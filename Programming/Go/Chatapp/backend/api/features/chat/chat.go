package chat

import (
	"github.com/batmanboxer/chatapp/models"
	"github.com/google/uuid"
)

type ChatStorage interface {
	GetMessages(string, int, int) ([]models.MessageModel, error)
	AddMessage(models.MessageModel) error
	CreateChatRoom([]uuid.UUID) error
	GetChatRoomsByUser(uuid.UUID) ([]*models.ChatRoom, error)
	RemoveUserFromRoom(int, int) error
}
