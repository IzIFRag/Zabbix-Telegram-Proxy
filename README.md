# Zabbix-Telegram-Proxy
Creating a proxy list and changing the script based on it

---------------
* Parsing ip of proxy and saves them to Proxy.txt.

* Through the list checks the availability of telegrams through a proxy and add proxy to script (/usr/lib/zabbix/alertscripts/).

* After a specified random interval checks the availability of the current proxy, if it is unavailable replaces it with the next, until the list ends.

### [To work needed jsoup library](http://webdesign.ru.net)

### To start:
    nohup java -cp jsoup-1.11.3.jar: TestProxy &
