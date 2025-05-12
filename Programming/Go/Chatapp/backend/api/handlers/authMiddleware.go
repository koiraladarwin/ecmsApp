package handlers

import (
	"context"
	"log"
	"net/http"
	"github.com/batmanboxer/chatapp/api/features/authentication"
	"github.com/batmanboxer/chatapp/common"
)

func (h *Handlers) AuthenticationMiddleware(next customHttpHandler) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			http.Error(w, "Authorization header required", http.StatusUnauthorized)
			return
		}

		userId, err := auth.ValidateJwt(authHeader)
		if err != nil {
			http.Error(w, "Invalid JWT", http.StatusUnauthorized)
		}
    log.Printf("userID = %s",userId)
		//also check if this user exists in userdatabase
		ctx := context.WithValue(r.Context(), common.CONTEXTIDKEY, userId)

    err = next(w, r.WithContext(ctx))
    if err != nil {
			log.Printf("error: %s\n", err.Error())
			http.Error(w, "Internal Server Error", http.StatusInternalServerError)
		}

	}
}
