const { createApp } = Vue;

createApp({
    data(){
        return {
            toggle: false,
            isNavVisible: false,
            data: '',
            cards: [],
            creditCards:[],
            debitCards:[],
            cardType:'Select a type...',
            cardColor:'Select a color...',
            typeErr: '',
            colorErr: '',
            alreadyCarErr:''
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        
        loadData(){
           
            axios.get('http://localhost:8080/api/clients/current')
                .then(response => {
                    this.data = response.data;
                    this.cards = this.data.cards;

                })
                .catch(err => console.log(err))
        },
       
        toggleBtn(){
            this.toggle = !this.toggle;
        },
        signOut(){
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = 'http://localhost:8080/web/views/index.html';
        },
        burgerMenu(){
            this.toggle = !this.toggle
            console.log(this.toggle)
        },
        createCard(){
            axios.post('/api/clients/current/cards',`cardType=${this.cardType}&cardColor=${this.cardColor}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(response => {
                    console.log(response)
                    window.location.href = 'http://localhost:8080/web/views/cards.html';
                })
                .catch(err => {
                    
                    let errorArr =  err.response.data.split("-")
                    console.log(errorArr)

                    if(errorArr.includes("you have choose a color of card")){
                        this.colorErr = "you have choose a color of card"
                    }else{
                        this.colorErr = ""
                    }
                    if(errorArr.includes("you have choose a type of card")){
                        this.typeErr = "you have choose a type of card"
                    }else{
                        this.typeErr = ""
                    }
                    if(errorArr.includes("You already have a card of that type")){
                        this.alreadyCarErr = "you already have that card"
                    }else{
                        this.alreadyCarErr = ""
                    }
                })
        }

    }
}).mount("#app")
