# Zabbix-Telegram-Proxy
Creating a proxy list and changing the script based on it

---------------
* Parsing ip of proxy and saves them to Proxy.txt.

* Through the list checks the availability of telegrams through a proxy and add proxy to script (/usr/lib/zabbix/alertscripts/).

* After a specified random interval checks the availability of the current proxy, if it is unavailable replaces it with the next, until the list ends.

### [To work needed jsoup library](https://jsoup.org/download)

### To start:
    nohup java -cp jsoup-1.11.3.jar: TestProxy &

### In script:
curl -x !Proxy adres here! --header 'Content-Type: application/json' --request 'POST' --data "{\"chat_id\":\"!TelegramID User1\",\"text\":\"${SUBJECT}\n${MESSAGE}\"}" "https://api.telegram.org/!telegram-bot token here!/sendMessage" | grep -q '"ok":false,'

#### where:  
* !TelegramID User1 - your Telegram id (Сan learn by writing to @my_id_bot) like 210806260
* !telegram-bot token here! - Telegram Bot and his Token like bot523584658:AAE5QmWYsbhrDRZaEtFcFw5bVj7VdZ17s4c
