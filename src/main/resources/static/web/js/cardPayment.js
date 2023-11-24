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
            cardNumber:'',
            cvv:'',
            amount: '',
            description:'',
            errors:'',
            numberErr:'',
            cvvErr:'',
            amountErr:'',
            descriptionErr: '',
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        
        loadData(){
           
            axios.get('https://localhost:8080/api/clients/current')
                .then(response => {
                    this.data = response.data;
                    this.cards = this.data.cards;
                    console.log(this.cards)

                    
                    this.data = response.data;
                    this.cards = this.data.cards;


                    this.creditCards = this.cards.filter(card => card.type == "CREDIT");
                    this.debitCards = this.cards.filter(card => card.type == "DEBIT");
                    
                    this.debitCards = this.cleanObject( this.debitCards)
                    this.creditCards = this.cleanObject( this.creditCards)


                    // extraigo la fecha de vencimiento de la card para comparar en el v-show y mostrar si expiró
                    const currentDate = new Date();
                    const currentMonth = currentDate.getMonth() + 1 // Sumamos 1 porque los meses se cuentan desde 0 (enero) hasta 11 (diciembre)
                    const currentYear = currentDate.getFullYear()
                    const currentDateFormat = `${currentMonth.toString().padStart(2, '0')}/${currentYear}` // padStart(2, '0') se utiliza para asegurarse de que el mes tenga dos dígitos, añadiendo un cero a la izquierda si es necesario
                    this.currentDate = currentDateFormat

                })
                .catch(err => console.log(err))
        },  
        cleanObject(card){
            let updateCard = card.map(element => ({
                 ...element,
                 number: element.number.replace(/-/g, ' '),
                 thruDate: `${element.thruDate.split('-')[1]}/${element.thruDate.split('-')[0]}`,
                 fromDate: `${element.fromDate.split('-')[1]}/${element.fromDate.split('-')[0]}`
            }))
            return updateCard;
         },
       
        toggleBtn(){
            this.toggle = !this.toggle;
        },
        signOut(){
            axios.post('https://localhost:8080/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = 'https://localhost:8080/web/views/index.html';
        },
        burgerMenu(){
            this.toggle = !this.toggle
            console.log(this.toggle)
        },
        createPayment(){
            axios.post('https://localhost:8080/api/clients/current/cards/payments',`cardNumber=${this.cardNumber}&cvv=${this.cvv}&amount=${this.amount}&description=${this.description}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(response => {
                    console.log(response)
                })
                .catch(err => {
                    console.log(err)
                    this.errors = err.response.data
                    let errorArr = this.errors.split("-")

                    console.log(errorArr)

                    if(errorArr.includes("card number is empty") || errorArr.includes("the card number should only have numbers")){
                        this.numberErr = "card not found, verify that you have entered the numbers correctly"
                     }else{
                         this.numberErr = ""
                     }
                     if(errorArr.includes("cvv number is empty") || errorArr.includes("the cvv should only have numbers")){
                         this.cvvErr = "cvv invalid"
                     }else{
                         this.cvvErr = ""
                     }
                     if(errorArr.includes("amount is empty") || errorArr.includes("the amount should only have numbers")){
                         this.amountErr = "amount invalid"
                     }else{
                         this.amountErr = ""
                     }
                     if(errorArr.includes("description is empty")){
                         this.descriptionErr = "description is empty"
                     }else{
                         this.descriptionErr = ""
                     }
                    
                })
                
        }

    }
}).mount("#app")
