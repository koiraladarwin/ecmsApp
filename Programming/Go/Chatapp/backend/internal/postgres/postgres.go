package postgress

import (
	"database/sql"
	"encoding/json"
	"github.com/batmanboxer/chatapp/models"
	"github.com/google/uuid"
	_ "github.com/lib/pq"
)

type Postgres struct {
	db *sql.DB
}

func NewPostGres(dbName string,connStr string) (*Postgres, error) {

	db, err := sql.Open(dbName, connStr)

	if err != nil {
		return nil, err
	}
	if err := db.Ping(); err != nil {
		return nil, err
	}
	execute := `CREATE TABLE IF NOT EXISTS users(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );`
	_, err = db.Exec(execute)
	if err != nil {
		return nil, err
	}

	execute = `CREATE TABLE IF NOT EXISTS chats(
    id SERIAL PRIMARY KEY,
    room_id TEXT NOT NULL,
    sender_id TEXT NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );`

	_, err = db.Exec(execute)
	if err != nil {
		return nil, err
	}

	execute = `CREATE TABLE IF NOT EXISTS chats_room(
	   id SERIAL PRIMARY KEY,
	   user_ids JSONB NOT NULL DEFAULT '[]',
	   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP) ;`

	_, err = db.Exec(execute)
	if err != nil {
		return nil, err
	}

	return &Postgres{
		db: db,
	}, nil
}

func (postgres *Postgres) AddAccount(signUpData models.SignUpData) error {
	//change password to password hash later and password should be a hash
	addAccountQuery := `INSERT INTO users(name,email,password)VAlUES($1,$2,$3)`
	_, err := postgres.db.Exec(addAccountQuery, signUpData.Name, signUpData.Email, signUpData.Password)

	return err
}

func (postgres *Postgres) GetUserByEmail(email string) (models.AccountModel, error) {
	account := models.AccountModel{}
	query := `SELECT * FROM users WHERE email = $1`
	err := postgres.db.QueryRow(query, email).Scan(&account.ID, &account.Name, &account.Email, &account.Password, &account.Verified, &account.CreatedAt)
	if err != nil {
		return account, err
	}
	return account, nil
}

func (postgres *Postgres) AddMessage(messageModel models.MessageModel) error {
	addAccountQuery := `INSERT INTO chats(room_id,sender_id,message)VAlUES($1,$2,$3)`
	_, err := postgres.db.Exec(addAccountQuery, messageModel.RoomId, messageModel.SenderId, messageModel.Message)

	return err
}

func (postgres *Postgres) GetMessages(chatRoomId string, limit int, offset int) ([]models.MessageModel, error) {
	Messages := []models.MessageModel{}
	query := `SELECT * FROM chats ORDER BY created_at DESC WHERE room_id = $1 LIMIT $2 OFFSET $3`
	rows, err := postgres.db.Query(query, chatRoomId, limit, offset)

	if err != nil {
		return nil, err
	}

	for rows.Next() {
		message := models.MessageModel{}
		rows.Scan(&message.Id, &message.RoomId, &message.SenderId, &message.Message, &message.CreatedAt)
		Messages = append(Messages, message)
	}
	return Messages, nil
}

func (pg *Postgres) CreateChatRoom(userIDs []uuid.UUID) error {
	query := `INSERT INTO chats_room (user_ids) VALUES ($1)`
	userIDsJSON, err := json.Marshal(userIDs)
	if err != nil {
		return err
	}
	_, err = pg.db.Exec(query, userIDsJSON)
	return err
}

func (pg *Postgres) GetChatRoomsByUser(userID uuid.UUID) ([]*models.ChatRoom, error) {
	query := `SELECT id, created_at, user_ids FROM chats_room WHERE user_ids @> $1`
	userIDJSON, err := json.Marshal([]string{userID.String()})
	if err != nil {
		return nil, err
	}

	rows, err := pg.db.Query(query, userIDJSON)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var chatRooms []*models.ChatRoom

	for rows.Next() {
		var room models.ChatRoom
		var userIDsJSON []byte

		err := rows.Scan(&room.ID, &room.CreatedAt, &userIDsJSON)
		if err != nil {
			return nil, err
		}

		err = json.Unmarshal(userIDsJSON, &room.UserIDs)
		if err != nil {
			return nil, err
		}

		chatRooms = append(chatRooms, &room)
	}

	return chatRooms, nil
}

func (pg *Postgres) RemoveUserFromRoom(roomID int, userID int) error {
	query := `
		UPDATE chats_room
		SET user_ids = (
			SELECT jsonb_agg(elem)
			FROM jsonb_array_elements(user_ids) AS elem
			WHERE elem != to_jsonb($1::int)
		)
		WHERE id = $2
	`
	_, err := pg.db.Exec(query, userID, roomID)
	return err
}
