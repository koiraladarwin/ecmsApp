package server

import (
	"fmt"
	"github.com/batmanboxer/chatapp/api/features/authentication"
	"github.com/batmanboxer/chatapp/api/features/chat"
	"github.com/batmanboxer/chatapp/api/handlers"
	"github.com/batmanboxer/chatapp/internal/database"
	"github.com/batmanboxer/chatapp/models"
	"github.com/gorilla/mux"
	"net/http"
	"sync"
)

type Api struct {
	port    string
	storage database.Storage
	conn    map[string][]*models.Client
	mutex   *sync.RWMutex
}

func NewApi(port string, storage database.Storage) *Api {
	return &Api{
		port:    port,
		storage: storage,
		conn:    make(map[string][]*models.Client),
		mutex:   &sync.RWMutex{},
	}
}

func (api *Api) StartApi() {
	AuthManager := auth.AuthManager{
		AuthDb: api.storage,
	}

	ChatManager := chat.WebSocketManager{
		ChatStorage: api.storage,
		Clients: map[string][]*models.Client{},
		Mutex:   sync.RWMutex{},
	}

	handlers := handlers.NewHandlers(
		&AuthManager,
		&ChatManager,
	)

	mux := mux.NewRouter()

	mux.HandleFunc("/login", handlers.DefaultMiddleware(handlers.LoginHandler))
	mux.HandleFunc("/signup", handlers.DefaultMiddleware(handlers.SignUpHandler))
	mux.HandleFunc("/validate", handlers.DefaultMiddleware(handlers.ValidateHanlder))
	mux.HandleFunc("/listen/{id}", handlers.AuthenticationMiddleware(handlers.WebsocketHandler))
	mux.HandleFunc("/addchatroom", handlers.AuthenticationMiddleware(handlers.AddChatRoomHanlder))
	mux.HandleFunc("/getchatrooms", handlers.AuthenticationMiddleware(handlers.GetUserChatRoomsHanlder))

	fmt.Print("Server Starting in Port 4000")
	http.ListenAndServe(":4000", mux)
}
