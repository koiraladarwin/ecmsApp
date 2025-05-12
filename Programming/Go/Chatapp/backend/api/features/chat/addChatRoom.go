package chat

import "github.com/google/uuid"


func (h *WebSocketManager) AddChatRoom(users []uuid.UUID) error {
  //todo make sure there is no existing chatroom between those users
  //todo make sure the other user is also valid and then only create chat room

 	h.ChatStorage.CreateChatRoom(users)

	return nil
}
