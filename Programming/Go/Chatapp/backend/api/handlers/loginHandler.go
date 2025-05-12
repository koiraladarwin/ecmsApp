package handlers

import (
	"encoding/json"
	"net/http"
	"github.com/batmanboxer/chatapp/internal/utils"
	"github.com/batmanboxer/chatapp/models"
)

func(h *Handlers) LoginHandler(w http.ResponseWriter, r *http.Request) error {
   
	if r.Method != http.MethodPost {
		w.WriteHeader(http.StatusMethodNotAllowed)
		w.Write([]byte("Method Not Allowed"))
		return nil
	}
	data := models.LoginData{}
	err := json.NewDecoder(r.Body).Decode(&data)

	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("Unknown Data Type Provided"))
		return nil
	}

	jwt, err := h.AuthManager.AuthLogin(data)

	if err != nil {
		w.WriteHeader(http.StatusUnauthorized)
		w.Write([]byte(err.Error()))
		return nil
	}

	w.WriteHeader(http.StatusOK)
	sucess := models.LoginSucess{
		Jwt: jwt,
	}
  err = utils.WriteJson(w,sucess)
  
  if err != nil{
    return nil 
  }
	return nil
}

