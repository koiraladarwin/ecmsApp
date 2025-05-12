package auth

import (
	"github.com/batmanboxer/chatapp/models"
)

type AuthStorage interface {
	AddAccount(models.SignUpData) error
	GetUserByEmail(string) (models.AccountModel, error)
}

type AuthManager struct {
	AuthDb AuthStorage
}
