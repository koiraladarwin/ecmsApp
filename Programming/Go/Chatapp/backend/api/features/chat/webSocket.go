package chat

import (
	"fmt"
	"log"
	"net/http"
	"sync"
	"time"
	"github.com/batmanboxer/chatapp/models"
	"github.com/gorilla/websocket"
)

type WebSocketManager struct {
	ChatStorage ChatStorage
	Clients map[string][]*models.Client
	Mutex   sync.RWMutex
}

var upgrader = websocket.Upgrader{
	CheckOrigin: func(r *http.Request) bool {
		return true
	},
}

func (h *WebSocketManager) addClient(chatRoomId string, client *models.Client) {
	h.Mutex.Lock()
	h.Mutex.Unlock()

	h.Clients[chatRoomId] = append((h.Clients[chatRoomId]), client)

	go h.handleMessages(client)
	//go h.testMsg(client)
	go h.listenMessage(chatRoomId, client)
}

func (h *WebSocketManager) initialMessage(chatRoomId string, client *models.Client, limit int) {
	messages, err := h.ChatStorage.GetMessages(chatRoomId, limit, 0)
	if err != nil {
		return
	}
	for _, message := range messages {
		client.Messagech <- message.Message
	}
}

func (h *WebSocketManager) removeClient(chatRoomId string, userId string) {
	h.Mutex.Lock()
	defer h.Mutex.Unlock()

	clients, ok := h.Clients[chatRoomId]
	if !ok {
		return
	}

	var updatedClients []*models.Client
	for _, client := range clients {
		if client.Id != userId {
			updatedClients = append(updatedClients, client)
		} else {
			if client.Messagech != nil {
				close(client.Messagech)
			}
		}
	}

	if len(updatedClients) == 0 {
		delete(h.Clients, chatRoomId)
	} else {
		h.Clients[chatRoomId] = updatedClients
	}
}

func (h *WebSocketManager) listenMessage(roomID string, client *models.Client) {
	//authorized client
	for {
		messageType, p, err := client.Conn.ReadMessage()
		if err != nil {
			client.Closech <- struct{}{}
			break
		}
		if messageType != websocket.TextMessage {
			continue
		}
		// message := models.Message{}
		// err = json.Unmarshal(p,&message)
		// if err != nil{
		//   client.Closech<-struct{}{}
		//   break
		// }

		h.broadcastMessage(roomID, string(p), client)
	}

}

func (h *WebSocketManager) broadcastMessage(roomId string, message string, client *models.Client) {
	h.Mutex.RLock()
	defer h.Mutex.RUnlock()

	clients, ok := h.Clients[roomId]
	if !ok {
		return
	}
	err := h.ChatStorage.AddMessage(models.MessageModel{
		RoomId:   roomId,
		Message:  message,
		SenderId: client.Id,
	})
	if err != nil {
		log.Println(err.Error())
		return
	}

	for _, client := range clients {
		if client.Messagech != nil {
			client.Messagech <- message
		}
	}
}

func(h *WebSocketManager) testMsg(client *models.Client) {
	for {
		client.Messagech <- "testing"
		time.Sleep(5 * time.Second)
	}
}

func (h *WebSocketManager) handleMessages(client *models.Client) {
	for message := range client.Messagech {
		err := client.Conn.WriteMessage(websocket.TextMessage, []byte(message))
		if err != nil {
			fmt.Println("Error sending message to client", client.Messagech, err)
			return
		}
	}
}

func (h *WebSocketManager) WebsocketAddClient(conn *websocket.Conn, chatRoomId string, stringUserId string) {
	client := &models.Client{
		Id:        stringUserId,
		Conn:      conn,
		Messagech: make(chan string),
		Closech:   make(chan struct{}),
	}

	h.addClient(chatRoomId, client)

	<-client.Closech
	conn.Close()
	h.removeClient(chatRoomId, stringUserId)
}
