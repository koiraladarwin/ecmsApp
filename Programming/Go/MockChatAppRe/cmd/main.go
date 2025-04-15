package main

import (
	"github.com/batmanboxer/mockchatappre/api/server"
	"github.com/batmanboxer/mockchatappre/models"
	"log"
)

type TestStorage struct {
}

func (t TestStorage) GetMessages(a string, b int, c int) ([]models.MessageModel, error) {
	log.Println("GetMessage")
	return []models.MessageModel{}, nil
}
func (t TestStorage) AddMessage(messageModel models.MessageModel) error {
	log.Println("AddMessage")
	return nil
}
func (t TestStorage) AddAccount(a models.SignUpData) error {
	log.Println("AddAccount")
	return nil
}
func (t TestStorage) GetUserByEmail(a string) (models.AccountModel, error) {
	return models.AccountModel{}, nil
}

func main() {
	Api := server.NewApi(":4000", TestStorage{})
	Api.StartApi()
}
