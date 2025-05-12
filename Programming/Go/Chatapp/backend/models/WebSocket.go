package models

import (
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/websocket"
)

type MessageData struct {
	Message string
}

type MessageModel struct {
	Id        string    `json:"id"`
	SenderId  string    `json:"sender_id"`
	RoomId    string    `json:"room_id"`
	Message   string    `json:"message"`
	CreatedAt time.Time `json:"created_at"`
}

type Client struct {
	Id        string
	Conn      *websocket.Conn
	Messagech chan string
	Closech   chan struct{}
	Mutex     *sync.RWMutex
}

type ChatRoom struct {
	ID        int   `json:"id"`
	CreatedAt time.Time `json:"created_at"`
	UserIDs   []uuid.UUID `json:"user_ids"`
}

type AddChatRoomRequest struct {
    Participant uuid.UUID `json:"participant"`
}
