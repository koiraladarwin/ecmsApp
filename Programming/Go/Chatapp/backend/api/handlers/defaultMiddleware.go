package handlers

import (
	"log"
	"net/http"
	"github.com/batmanboxer/chatapp/models"
	"github.com/google/uuid"
	"github.com/gorilla/websocket"
)

type ChatService interface {
	WebsocketAddClient(conn *websocket.Conn, chatRoomId string, userId string)
	AddChatRoom(users []uuid.UUID) error
	GetChatRoomsByUser(uuid.UUID) ([]*models.ChatRoom, error)
}

type AuthService interface {
	AuthLogin(models.LoginData) (string, error)
	AuthSignUp(models.SignUpData) error
}

type Handlers struct {
	AuthManager AuthService
	ChatManager ChatService
}

func NewHandlers(
	authService AuthService,
	ChatService ChatService,
) *Handlers {
	return &Handlers{
		AuthManager: authService,
		ChatManager: ChatService,
	}
}

type customHttpHandler func(http.ResponseWriter, *http.Request) error

func (h *Handlers) DefaultMiddleware(customHandler customHttpHandler) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		err := customHandler(w, r)
		if err != nil {
			log.Printf("error: %s\n", err.Error())
			http.Error(w, "Internal Server Error", http.StatusInternalServerError)
		}
	}
}
