package handlers

import (
	"errors"
	"net/http"
	"github.com/batmanboxer/chatapp/common"
	"github.com/batmanboxer/chatapp/internal/utils"
	"github.com/batmanboxer/chatapp/models"
	"github.com/google/uuid"
)

func (h *Handlers) AddChatRoomHanlder(w http.ResponseWriter, r *http.Request) error {

	userId := r.Context().Value(common.CONTEXTIDKEY)
	stringUserId, ok := userId.(string)

 	if !ok {
		return errors.New("User Id Invalid")
	}
 
	user1, err := uuid.Parse(stringUserId)
	if err != nil {
		return err
	}

	user2 := models.AddChatRoomRequest{}

	utils.ReadJson(r, &user2)

	users := []uuid.UUID{}

	users = append(users, user1)
	users = append(users, user2.Participant)

	err = h.ChatManager.AddChatRoom(users)

	if err != nil {
		return err
	}

	//todo make a custom writer to the http connection
	w.Write([]byte("done"))


	return nil
}
