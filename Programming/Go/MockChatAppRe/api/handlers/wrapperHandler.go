package handlers

import (
	"context"
	"github.com/batmanboxer/mockchatappre/api/websocket"
	"github.com/batmanboxer/mockchatappre/common"
	"github.com/batmanboxer/mockchatappre/internals/authentication"
	"log"
	"net/http"
)

type Handlers struct {
	WebSocketManager *websocket.WebSocketManager
	AuthManager      *auth.AuthManager
}

func NewHandlers(
	authManager *auth.AuthManager,
	webSocketManager *websocket.WebSocketManager,
) *Handlers {
	return &Handlers{
		WebSocketManager: webSocketManager,
		AuthManager:      authManager,
	}
}

type customHttpHandler func(http.ResponseWriter, *http.Request) error

func (h *Handlers) WrapperHandler(customHandler customHttpHandler) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		err := customHandler(w, r)
		if err != nil {
			log.Printf("error: %s\n", err.Error())
			http.Error(w, "Internal Server Error", http.StatusInternalServerError)
		}
	}
}

func (h *Handlers) AuthenticationMiddleware(next http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// authHeader := r.Header.Get("Authorization")
		// if authHeader == "" {
		// 	http.Error(w, "Authorization header required", http.StatusUnauthorized)
		// 	return
		// }
		//
		// userId, err := auth.ValidateJwt(authHeader)
		// if err != nil {
		// 	http.Error(w, "Invalid JWT", http.StatusUnauthorized)
		// }
		// //also check if this user exists in userdatabase
		//remove this fake userId later
		userId := "test"
		ctx := context.WithValue(r.Context(), common.CONTEXTIDKEY, userId)

		next(w, r.WithContext(ctx))
	}
}
