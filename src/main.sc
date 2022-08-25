require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
require: core.js
  
theme: /

    state: Start
        q!: $regex</start>
        intent!: /Старт
        go!: /Register
    
    state: Hello
        intent!: /привет
        go!: /Register


    state: Register
        a: Здравствуй будущий селлер! \nТы сделал очень правильный выбор придя на подготовительный вебинар! Совсем скоро ты будешь готов стать настоящим селлером самой популярной площадки страны!  
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
            state: 1
                q: * @duckling.email *
                script: 
                    $client.mail = $parseTree.value;
                go!: /Register/GetPhone

            state: 2
                event: noMatch
                a: Мне кажется email введен с ошибкой, проверь пожалуйста и введи снова
                go!: /Register/GetMail/1


        state: GetPhone
            a: Отлично, напиши твой номер телефона, чтобы я мог тебе позвонить в любое время дня и ночи! Я шучу, я не буду звонить, мне нужно для занесения в систему.
            state: 1
                q: * @duckling.phone-number *
                script: $client.phone = $parseTree.value;
                go!: /Register/Finish

            state: 2
                event: noMatch
                a: Мне кажется телефон введен с ошибкой, проверь пожалуйста и введи снова
                go!: /Register/GetPhone/1
                
        state: Finish
            a: Все получилось! Теперь давай с тобой посмотрим, что мы можем сделать полезного перед нашим вебинаром, чтобы он прошел максимально эффективно, независимо от того, пойдешь ли ты на дальнейшее обучение.
            buttons:
                        "Давай!" -> /Motivation


    state: Motivation
        a: Ты пришел ко мне со своей мечтой научиться работать с вайлдбериз, если ты не умеешь или создать свой бренд в дальнейшем. Поэтому давай мы с тобой напишем список всего, что тебе нравится и тебя вдохновляет. Нам он понадобится в дальнейшем. 
        buttons:
                        "Давай!" -> /Motivation/1
        state: 1
            a: А теперь скажи самому себе: Я ГОТОВ ЕБ@ШИТЬ РАДИ СВОЕЙ МЕЧТЫ 
            script:
                $response.replies = $response.replies || [];
                $response.replies.push({
                    "type": "inlineButtons",
                    "buttons": [{"text": "Я готов!", "url": "https://705402.selcdn.ru/bot_storage/1/bashit.jpg"}]
                });
                


    state: Stupid
        a: Ну и на этом мои полномочия как-бы все


    state: NoMatch || noContext = true
        event!: noMatch
        random:
            a: Я не понял.
            a: Что вы имеете в виду? 
            a: Ничего не пойму.






        

        
        
        