'use strict';

const usernamePage = document.querySelector('#username-page');
const homePage = document.querySelector('#home-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const connectingElement = document.querySelector('.connecting');
const chatListElement = document.querySelector('.chat-list div');

let stompClient = null;
let username = null;

const colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if (username) {
        loadChatList();

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnect, onError);
    }

    event.preventDefault();
}

function loadChatList() {
    fetchChatList().then(payload => {
        for (let i = 0; i < payload.length; i++) {
            onChatReceived(payload[i], i);
        }
    })
    usernamePage.classList.add('hidden');
    homePage.classList.remove('hidden');
}

function onChatReceived(chat, shift) {
    let chatContainer = document.createElement('div');
    chatContainer.classList.add('ListItem', 'Chat', 'chat-item-clickable');

    let chatElement = document.createElement('a');
    chatElement.href = `chats/${chat.id}`;
    chatElement.role = 'button';
    chatElement.tabIndex = 0;
    chatElement.classList.add('ListItem-button');

    let avatarContainer = document.createElement('div');
    avatarContainer.classList.add('status');
    let avatarElement = document.createElement('div');
    avatarElement.classList.add('Avatar');
    let avatarText = document.createTextNode(chat.title[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(chat.title);
    avatarContainer.appendChild(avatarElement);

    let chatInfoContainer = document.createElement('div');
    chatInfoContainer.classList.add('info');
    let titleContainer = document.createElement('div');
    titleContainer.classList.add('title');
    let titleElement = document.createElement('span');
    let titleText = document.createTextNode(chat.title);
    titleElement.appendChild(titleText);
    titleContainer.appendChild(titleElement);
    chatInfoContainer.appendChild(titleContainer);

    chatElement.appendChild(avatarContainer);
    chatElement.appendChild(chatInfoContainer);
    chatContainer.appendChild(chatElement);
    chatContainer.style['top'] = `${72 * shift}px`;

    chatListElement.appendChild(chatContainer);
}

function onConnect() {
    // subscribe to the public topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // pass username to the server
    stompClient.send(
        '/app/chat.addUser',
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    );

    connectingElement.classList.add('hidden')
    fetchChatHistory().then(payload => {
        for (let i = 0; i < payload.length; i++) {
            onMessageReceived(payload[i], false);
        }
    });
}

function onMessageReceived(payload, removeHeaders = true) {
    const message = removeHeaders ? JSON.parse(payload.body) : payload;

    let messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        let avatarElement = document.createElement('i');
        let avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        let usernameElement = document.createElement('span');
        let usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    let textElement = document.createElement('p');
    let messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

const fetchChatList = async () => {
    const response = await fetch('http://localhost:8080/chat.getList');
    return await response.json();
}

const fetchChatHistory = async () => {
    const response = await fetch('http://localhost:8080/chats/start');
    return await response.json();
}

function getAvatarColor(messageSender) {
    let hash = 0;
    for (let i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    const index = Math.abs(hash % colors.length);
    return colors[index];
}

function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server'
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {

        const chatMessage = {
            sender: username,
            content: messageContent,
            type: 'CHAT'
        };
        stompClient.send(
            '/app/chat.sendMessage',
            {},
            JSON.stringify(chatMessage)
        );
        messageInput.value = '';
    }
    event.preventDefault();
}

