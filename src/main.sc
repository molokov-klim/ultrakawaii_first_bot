require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /

    state: Start || modal = true
        q!: $regex</start>
        script:
            $context.name = {};
            $context.mail = {};
        a: Здаровати! 🤗
        buttons:
            "Здаровати!" -> ./WhatsYourName
            
    state: WhatsYourName
    a: Напишите ваше имя
    q: *
    go!: /RememberName
        
        state: RememberName
            script:
                $client.name = $request.query;
            go!: /WhatsYourMail

    state: WhatsYourMail
    a: Напишите ваш email
    q: *
    go!: /RememberMail
        
        state: RememberMail
            script:
                $client.mail = $request.query;
            go!: /ChoosePizza

    state: Stupid
        a: Ну все, больше я нихера не умею! 🤗


    state: Hello
        intent!: /привет
        a: Здаровати! 🤗

    state: Bye
        intent!: /пока
        a: Пока 😢

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}

    state: Match
        event!: match
        a: {{$context.intent.answer}}
        
    
        
        
        