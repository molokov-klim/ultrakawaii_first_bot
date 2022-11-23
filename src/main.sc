require: slotfilling/slotFilling.sc
    module = sys.zb-common
  
require: common.js
    module = sys.zb-common
  
require: core.js
  
theme: /

    state: Start
        q!: $regex</start>
        intent!: /Старт
        go!: /Register
    

    state: Register
        a: Здравствуй будущий селлер! Ты сделал очень правильный выбор придя на подготовительный вебинар! Совсем скоро ты будешь готов стать настоящим селлером самой популярной площадки страны!   
        buttons:
            "Привет!" -> ./GetName


        state: GetName
            a: Давай с тобой познакомимся. Меня зовут Каваюша. Я бот-помощник на курсе Молоковой Анны. Я буду твоим верным помощником. Как тебя зовут?
            go!: /Register/GetName/Receive
            state: Receive
                state: 1
                    q: * @pymorphy.name *
                    script: $client.name = $parseTree.value
                    go!: /Register/GetName/Receive/1/Compliment
                    
                    state: Compliment
                        a: Какое классное имя, {{ $client.name }}! Человек с таким именем рожден быть успешным!
                        buttons:
                            "Спасибо!" -> /Register/GetMail
                            "Взаимно!" -> /Register/GetMail
                            
                        state: ClickButtons
                            q: *
                            a: Нажмите, пожалуйста, кнопку.
                            go!: ..    
                            
                state: 2
                    event: noMatch
                    a: Не балуйтесь. Напишите ваше имя
                    go!: /Register/GetName/Receive

        state: GetMail
            a: {{ $client.name }}, напиши мне свой email, чтобы я мог присылать тебе полезные материалы. Обещаю не спамить!
            go!: /Register/GetMail/Receive
            state: Receive
                state: 1
                    q: * @duckling.email *
                    script: 
                        $client.mail = $parseTree.value;
                    go!: /Register/GetPhone
    
                state: 2
                    event: noMatch
                    a: Мне кажется email введен с ошибкой, проверь пожалуйста и введи снова
                    go!: /Register/GetMail/Receive


        state: GetPhone
            a: Отлично, напиши твой номер телефона, я тебе позвоню поболтать! Я шучу, я не буду звонить! Это мне нужно для занесения в систему.
            go!: /Register/GetPhone/Receive
            state: Receive
                state: 1
                    q: * @duckling.phone-number *
                    script: $client.phone = $parseTree.value;
                    go!: /Register/Finish
    
                state: 2
                    event: noMatch
                    a: Мне кажется телефон введен с ошибкой, проверь пожалуйста и введи снова
                    go!: /Register/GetPhone/Receive
                
        state: Finish
            a: Все получилось! Теперь давай с тобой посмотрим, что мы можем сделать полезного перед нашим вебинаром, чтобы он прошел максимально эффективно, независимо от того, пойдешь ли ты на дальнейшее обучение.

            buttons:
                    "Давай!" -> /Register/Motivation
            
            state: ClickButtons
                q: *
                a: Нажмите, пожалуйста, кнопку.
                go!: ..   


        state: Motivation
            a: Ты пришел ко мне со своей мечтой стать селлером вайлдбериз и создать свой крутой бренд. Поэтому давай мы с тобой напишем список из пяти пунктов, что тебе нравится и тебя вдохновляет. Нам он понадобится в дальнейшей работе над твоим проектом. 
            state: 1
                q: *
                script:
                    $client.wishlist = $parseTree.value;
                    var user = $request.userFrom['id']
                    $session.user = user
                go!: /Register/Motivation/1/ebsh
                
                state: ebsh
                    a: А теперь скажи самому себе: Я ГОТОВ!  
                    GoogleSheets:
                        operationType = writeDataToLine
                        integrationId = 105169ae-0c15-4461-9f71-3d7c96451cd1
                        spreadsheetId = 1oQD0ERcCUQ0C22GTc67870Ljj-y6LWn-9jPCpXRKX8c
                        sheetName = 1
                        body = {"values":["{{ $session.user }}", "{{ $client.name }}", "{{ $client.phone }}", "{{ $client.mail }}", "{{ $client.wishlist }}"]}
                        okState = /Register/Motivation/1/ebsh/button
                        errorState = /Register/Motivation/1/ebsh/button
                    
                    
                    state: button
                        buttons:
                            "Я готов!" -> /Register/Motivation/1/Stories
                        
                    state: ClickButtons
                        q: *
                        a: Нажмите, пожалуйста, кнопку.
                        go!: ..   
                    

                state: Stories
                    a: Отмечай меня в сториз и я подарю тебе один полезный подарочек! 
                    script:
                                    $response.replies = $response.replies || [];
                                    $response.replies.push({
                                        "type": "image",
                                        "imageUrl": "https://705402.selcdn.ru/bot_storage/1/nevertryneverknow.jpg"
                                    });
                                  
                                  
                    buttons:   
                        "Идет!" -> /Register/Motivation/1/Gift
                        
                    state: ClickButtons
                            q: *
                            a: Нажмите, пожалуйста, кнопку.
                            go!: ..   
                    
                    
                    
                state: Gift
                    a: Уже отметил? Держи твой подарочек! Таблица запросов по категориям - стыкуем идею со спросом! Изучи табличку и готовься к вебинару 26-27 ноября! https://docs.google.com/spreadsheets/d/1vIEVjzH5SAnNt9HF5qJeM8CDiOHUmGJ1/edit?usp=sharing&ouid=107813419182735337605&rtpof=true&sd=true
                    
                    state: Спасибо
                        intent: /Благодарность
                        a: Пожалуйста! Я тебе всегда рад! Приходи еще и приводи друзей!

    state: NoMatch || noContext = true
        event!: noMatch
        random:
            a: Каваюша не понимает:(
            a: Введи /start чтобы начать заново


    state: Obscene
        q!: * @mlps-obscene.obscene *
        a: Сам ты вонючее хуйло. Начнем заново
        go!: /Register


    state: readDataFromCells
        intent: /readDataFromCells
        a: readDataFromCells
        
        GoogleSheets:
            operationType = readDataFromCells
            integrationId = 105169ae-0c15-4461-9f71-3d7c96451cd1
            spreadsheetId = 1oQD0ERcCUQ0C22GTc67870Ljj-y6LWn-9jPCpXRKX8c
            sheetName = 1
            body = [{"varName":"ID","cell":"A1"}, {"varName":"Name","cell":"B1"}, {"varName":"Phone","cell":"C1"}, {"varName":"Mail","cell":"D1"}, {"varName":"Telegram","cell":"E1"}]
            okState = /readDataFromCells/1
            errorState = /readDataFromCells/2
        
        state: 1
            a: {{ $session.ID }}, {{ $session.Name }}, {{ $session.Phone }}, {{ $session.Mail }}, {{ $session.Telegram }}
            
        state: 2
            a: Error
            
            
    state: writeDataToLine
        intent: /writeDataToLine
        a: writeDataToLine
        
        GoogleSheets:
            operationType = writeDataToLine
            integrationId = 105169ae-0c15-4461-9f71-3d7c96451cd1
            spreadsheetId = 1oQD0ERcCUQ0C22GTc67870Ljj-y6LWn-9jPCpXRKX8c
            sheetName = 1
            body = {"values":["None", "None", "{{ $client.name }}", "{{ $client.phone }}", "{{ $client.mail }}", "{{ $client.wishlist }}"]}
            okState = /writeDataToLine/1
            errorState = /writeDataToLine/2
        
        
        state: 1
            a: ok
            
        state: 2
            a: Error
        
        
    state: test
        q!: test
        script:
                var user = $request.userFrom['id']
                for (var i = 0; i < 1; i++) {
                    $session.user[i] = JSON.stringify($request.userFrom, null, 4)
                }
                
                
                $response.replies = $response.replies || [];
                $response.replies.push({
                            "type": "text",
                            "text": user
                        });
                
                
                
                
                    
                    
                    
                    