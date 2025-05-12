package handlers

import (
	"errors"
	"net/http"

	"github.com/batmanboxer/chatapp/common"
	"github.com/batmanboxer/chatapp/internal/utils"
	"github.com/google/uuid"
)

func (h *Handlers) GetUserChatRoomsHanlder(w http.ResponseWriter, r *http.Request) error {

	userId := r.Context().Value(common.CONTEXTIDKEY)
	stringUserId, ok := userId.(string)

	if !ok {
		return errors.New("User Id Invalid")
	}

	uuidUserId, err := uuid.Parse(stringUserId)
	if err != nil {
		return err
	}

	chatRooms, err := h.ChatManager.GetChatRoomsByUser(uuidUserId)
	if err != nil {
		return err
	}

	utils.WriteJson(w, chatRooms)

	return nil
}
