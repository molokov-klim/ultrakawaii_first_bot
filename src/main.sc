require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /

    state: Start
        q!: $regex</start>
        intent!: /Старт
        go!: /Register
    
    state: Hello
        intent!: /привет
        go!: /Register


    state: Register
        a: Здаровати! 🤗
        buttons:
            "Здаровати!" -> ./GetName


        state: GetName
            a: Напишите ваше имя
            state:
                q: * @pymorphy.name *
                script: $client.name = $parseTree.value
                go!: /Register/GetMail

            state:
                event: noMatch
                a: Не балуйтесь
                go!: /Register/GetName

        state: GetMail
            a: {{ $client.name }}, напишите ваш email
            state:
                q: * @duckling.email *
                script: 
                    $client.mail = $parseTree.value;
                a: Зафиксировал: {{ $client.name }}, {{ $client.mail }}
                go!: /Stupid

            state:
                event: noMatch
                a: Не балуйтесь
                go!: /Register/GetMail

    state: Stupid
        a: Ну и на этом мои полномочия как-бы все


    state: NoMatch || noContext = true
        event!: noMatch
        random:
            a: Я не понял.
            a: Что вы имеете в виду? 
            a: Ничего не пойму.







        

        
        
        