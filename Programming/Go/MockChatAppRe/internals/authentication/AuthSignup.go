package auth

import "github.com/batmanboxer/mockchatappre/models"

func (auth AuthManager) AuthSignUp(signUpData models.SignUpData) error {
	error := auth.AuthDb.AddAccount(signUpData)

	if error != nil {
		return error
	}

	return nil
}
