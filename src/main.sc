require: slotfilling/slotFilling.sc
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
            a: Давай с тобой познакомимся. Меня зовут Каваюша. Я бот на курсе Молоковой Анны. Я буду твоим верным помощником. Как тебя зовут?
            state: 1
                q: * 
                script: $client.name = $parseTree.text 
                go!: /Register/GetName/1/Compliment

                state: Compliment
                    a: Какое классное имя, {{ $client.name }}! Человек с таким именем рожден быть успешным!
                    buttons:
                        "Да!" -> /Register/GetMail
                

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
            a: Отлично, напиши твой номер телефона, я тебе позвоню поболтать! Я шучу, я не буду звонить, мне нужно для занесения в систему.
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


        state: Motivation
            a: Ты пришел ко мне со своей мечтой стать селлером вайлдбериз и создать свой крутой бренд. Поэтому давай мы с тобой напишем список из пяти пунктов, что тебе нравится и тебя вдохновляет. Нам он понадобится в дальнейшей работе над твоим проектом. 
            state: 1
                q: *
                script:
                    $client.wishlist = $parseTree.text 
                go!: /Register/Motivation/1/ebsh
            
                state: ebsh
                    a: А теперь скажи самому себе: Я ГОТОВ ЕБ@ШИТЬ РАДИ СВОЕЙ МЕЧТЫ 
                    buttons:
                        "Я готов!" -> /Stories
                    

    state: Stories
        a: Отмечай меня в сториз и я подарю тебе один полезный подарочек!
        script:
                        $response.replies = $response.replies || [];
                        $response.replies.push({
                            "type": "image",
                            "imageUrl": "https://705402.selcdn.ru/bot_storage/1/bashit2.jpg"
                        });
                        
     buttons:
        "Идет!" -> /Register/Gift
    
     

    state: Gift
        a: Уже отметил? Держи твой подарочек! Таблица запросов по категориям - стыкуем идею со спросом! https://docs.google.com/spreadsheets/d/1vIEVjzH5SAnNt9HF5qJeM8CDiOHUmGJ1/edit?usp=sharing&ouid=107813419182735337605&rtpof=true&sd=true
        

    state: NoMatch || noContext = true
        event!: noMatch
        random:
            a: Каваюша не понимает:(
            a: Что вы имеете в виду? 
            a: Ничего не пойму.






        

        
        
        