const { createApp } = Vue;

createApp({
    data(){
        return {
            data: '',
            accounts: [],
            toggle: false,
            transactions: [],
            account: '',
        }
    },
    created(){
        let urlString = location.search;
            let parameters = new URLSearchParams(urlString);
            let id = parameters.get('id');

            this.clientID = id;

            axios.get(`/api/accounts/${id}`)
                .then(response => {
                    let transactions = response.data.transactions;
                    this.transactions = transactions.sort((a, b) => b.id - a.id); // ordeno las transacciones por id de manera desendiente
                    this.account = response.data.number;
                    console.log(this.transactions)
                })
                .catch(err => console.log(err))
    },
    mounted() {
        AOS.init();
    },
    methods:{
        burgerMenu(){
            this.toggle = !this.toggle
            console.log(this.toggle)
        },
        signOut(){
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
                .then(response => console.log(response))
                .catch(err => console.log(err))
            window.location.href = 'https://mindhubbank-production-6712.up.railway.app/web/views/index.html';
            console.log('log out')
        }
    }
    
}).mount("#app")
