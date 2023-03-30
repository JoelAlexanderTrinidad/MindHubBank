const { createApp } = Vue;

createApp({
    data(){
        return {
            toggle: false,
            data: '',
            cards: [],
            creditCards:[],
            debitCards:[],
            here: false,
            currentDate: '',
        }
    },
    created(){
        this.loadData();
    },
    mounted() {
        AOS.init();
    },
    methods:{
        loadData(){
            axios.get('/api/clients/current')
                .then(response => {

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
        burgerMenu(){
            this.toggle = !this.toggle
            console.log(this.toggle)
        },
        signOut(){
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = 'https://mindhubbank-production-6712.up.railway.app/web/views/index.html';
        },
        hereBtn(){
            this.here = !this.here
            console.log(this.here)
        },
        createCard(){
            window.location.href = 'https://mindhubbank-production-6712.up.railway.app/web/views/create-cards.html';
        },
        deleteCard(id){

            Swal.fire({
                title: 'Are you sure you want to delete this card?',
                // text: `Are you sure you want to transfer $${this.amount} from ${this.numberAccountSource} to ${this.numberAccountDestination}?`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#fe08a4',
                cancelButtonColor: '#c40e0e',
                confirmButtonText: 'Yes, delete!'
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.post(`/api/clients/current/cards/${id}`)
                    .then(response => {
                        Swal.fire(
                            'Card deleted!',
                            'Your card has been successfully deleted',
                            'success'
                          )
                          setTimeout(() => {
                              window.location.reload();
                          }, 2300)
                    })
                    .catch(err => console.log(err))
                }
              })
        }

    }
}).mount("#app")
