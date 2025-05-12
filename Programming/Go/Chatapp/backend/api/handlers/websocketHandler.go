package handlers

import (
	"errors"
	"github.com/batmanboxer/chatapp/common"
	"github.com/gorilla/mux"
	"github.com/gorilla/websocket"
	"net/http"
)

var upgrader = websocket.Upgrader{
	CheckOrigin: func(r *http.Request) bool {
		return true
	},
}

func (h *Handlers) WebsocketHandler(w http.ResponseWriter, r *http.Request) error {
	vars := mux.Vars(r)
	chatroomId := vars["id"]
	userId := r.Context().Value(common.CONTEXTIDKEY)
	stringUserId, ok := userId.(string)

	if !ok {
		return errors.New("User Id Invalid")
	}

	//todo : check is chatroomid exists and if user is allowed in that chat room

	conn, err := upgrader.Upgrade(w, r, nil)

	if err != nil {
		return err
	}

	h.ChatManager.WebsocketAddClient(conn, chatroomId, stringUserId)
	return nil
}
