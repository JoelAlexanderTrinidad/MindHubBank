const { createApp } = Vue;

createApp({
    data(){
        return {
            data: '',
            accounts: [],
            toggle: false,
            accountNumber: '',
            accountType:'',
            errAccountType: '',
            admin: false,
        }
    },
    created(){
        axios.get('/api/clients/current')
            .then(response => {
                this.data = response.data;
                this.accounts = response.data.accounts.filter(account => account.eliminated == false)
                this.admin = response.data.email == "admin@admin.com"
                
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
        createAccount(){

            if(this.accountType == "Select a new type account" || this.accountType == ''){
                return this.errAccountType = "you must select an account type"
            }else{
                this.errAccountType = ""
            }

            console.log(this.accountType)


            Swal.fire({
                title: 'Create a new account?',
                // text: `Are you sure you want to transfer $${this.amount} from ${this.numberAccountSource} to ${this.numberAccountDestination}?`,
                showCancelButton: true,
                confirmButtonColor: '#fe08a4',
                cancelButtonColor: '#737373',
                confirmButtonText: 'Yes, create!'
              })
              .then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/clients/current/accounts',`accountType=${this.accountType}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        Swal.fire(
                            'Account created!',
                            'Your account has been successfully created',
                            'success'
                          )
                          setTimeout(() => {
                              window.location.reload();
                          }, 2200)
                    })
                    .catch(err => {
                        console.log(err)
                    })
                }
            })

        },
        deleteAccount(number){

            Swal.fire({
                title: 'Are you sure you want to delete the account?',
                // text: `Are you sure you want to transfer $${this.amount} from ${this.numberAccountSource} to ${this.numberAccountDestination}?`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#fe08a4',
                cancelButtonColor: '#737373',
                confirmButtonText: 'Yes, delete!'
              })
              .then((result) => {
                if (result.isConfirmed) {

                    axios.post(`/api/accounts/delete/${number}`)
                    .then(response => {
                        Swal.fire(
                            'Account deleted!',
                            'Your account has been successfully deleted',
                            'success'
                          )
                          setTimeout(() => {
                              window.location.reload();
                          }, 2200)
                        console.log(response)
                    })
                    .catch(err => console.log(err))
                  
                }
              })
        },
        signOut(){
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
                .then(response => console.log(response))
                .catch(err => console.log(err))
            window.location.href = 'http://localhost:8080/web/views/index.html';
            console.log('log out')
        }
    }
    
}).mount("#app")
