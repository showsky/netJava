介紹
---

這是一個 Java 實作多人聊天的專案，用來練習學習的 Java 所實作的專案

共有以下主要程式

* Server.java
* Handle.java
* serverCommand.java
* serverGUI.java
* clitentGUI.java

備註
---

1. socket 中的 isClosed 可以用來判斷 socket 有沒有關閉，但是要是 socket 從來都沒連接過也會顯示 false 。
2. isConnected 可以用來配段連接，但是他的意思是說這個 socket 是否有沒有連接過，要是有連接過就會都顯示 true 不管是否有沒有斷線過。
3. 利用 isClosed 和 isConneced 組合就能來判斷 socket 是否關閉。
4. 可以利用 in.getInputStream().read() == -1 來判斷對方是否有沒有斷線。

作者
---

Cheng Ting
showsky@gmail.com

文件
---

http://goo.gl/EgbX9	
