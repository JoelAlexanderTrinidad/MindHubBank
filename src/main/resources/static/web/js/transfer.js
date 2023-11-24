const { createApp } = Vue;

createApp({
    data(){
        return {
            toggle: false,
            isNavVisible: false,
            data: '',
            accounts: [],
            transfType: false,
            numberAccountSource: '',
            numberAccountDestination:'',
            source:'',
            destination:'',
            amount: '',
            description: '',
            errors:'',
            sourceErr:'',
            destinationErr:'',
            sourceMissingErr:'',
            destinationMissingErr:'',
            descriptionMissingErr:'',
            amountErr:'',
            negativeAmountErr:'',
            amountExceeded: '',
            sameAcc: '',
        }
    },
    created(){
        this.loadData();
        
    },
    mounted() {
        this.transfType = true
    },
    methods:{
        loadData(){

            axios.get('/api/clients/current')
                .then(response => {
                    this.data = response.data;
                    this.cards = this.data.cards;
                    this.accounts = response.data.accounts.filter(account => account.eliminated == false)

                })
                .catch(err => console.log(err))
        },
        toggleNav() {
            this.isNavVisible = !this.isNavVisible;
        },
        close(){
            this.isNavVisible = false;
        },
        toggleBtn(){
            this.toggle = !this.toggle;
        },
        signOut(){
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = '/web/views/index.html';
        },
        burgerMenu(){
            this.toggle = !this.toggle
            console.log(this.toggle)
        },
        createTransfer(){

            console.log(this.transfType)

            

            if(this.transfType == 'own'){

                  
                    Swal.fire({
                        title: 'Are you sure you want to make the transaction??',
                        // text: `Are you sure you want to transfer $${this.amount} from ${this.numberAccountSource} to ${this.numberAccountDestination}?`,
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: '#fe08a4',
                        cancelButtonColor: '#c40e0e',
                        confirmButtonText: 'Yes, transfer!'
                      }).then((result) => {
                        if (result.isConfirmed) {


                            axios.post('/api/transactions',`amount=${this.amount}&description=${this.description}&sourceAccount=${this.numberAccountSource}&destinationAccount=${this.numberAccountDestination}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                            .then(response => {
                                Swal.fire(
                                    'Amount transferred!',
                                    'Your amount has been successfully transferred',
                                    'success'
                                  )
                                  setTimeout(() => {
                                      window.location.reload();
                                  }, 2300)
                            })
                            .catch(err => {
                                this.errors = err.response.data

                                console.log(this.errors)
                                let errorArr = this.errors.split("-")

                                if(errorArr.includes("source account is missing")){
                                   this.sourceErr = "source account is missing"
                                }else{
                                    this.sourceErr = ""
                                }
                                if(errorArr.includes("destination account is missing")){
                                    this.destinationErr = "destination account is missing"
                                }else{
                                    this.destinationErr = ""
                                }
                                if(errorArr.includes("must enter an amount")){
                                    this.amountErr = "must enter an amount"
                                }else{
                                    this.amountErr = ""
                                }
                                if(errorArr.includes("cannot enter a negative amount")){
                                    this.negativeAmountErr = "cannot enter a negative amount"
                                }else{
                                    this.negativeAmountErr = ""
                                }
                                if(errorArr.includes("description is missing")){
                                    this.descriptionMissingErr = "description is missing"
                                }else{
                                    this.descriptionMissingErr = ""
                                }
                                if(errorArr.includes("The amount cannot be less than the account balance")){
                                    this.amountExceeded = "The amount cannot be less than the account balance"
                                }else{
                                    this.amountExceeded = ""
                                }
                                if(errorArr.includes("accounts are the same")){
                                    this.sameAcc = "accounts are the same"
                                }else{
                                    this.sameAcc = ""
                                }
                                console.log(errorArr)
                            })
                          
                        }
                      })
               
            }else{

                Swal.fire({
                    title: 'Are you sure you want to make the transaction?',
                    // text: `Are you sure you want to transfer $${this.amount} from ${this.numberAccountSource} to ${this.numberAccountDestination}?`,
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#fe08a4',
                    cancelButtonColor: '#c40e0e',
                    confirmButtonText: 'Yes, transfer!'
                  }).then((result) => {
                    if (result.isConfirmed) {
                        axios.post('/api/transactions',`amount=${this.amount}&description=${this.description}&sourceAccount=${this.numberAccountSource}&destinationAccount=${this.destination}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                        .then(response => {
                            Swal.fire(
                                'Amount transferred!',
                                'Your amount has been successfully transferred',
                                'success'
                              )

                              setTimeout(() => {
                                window.location.reload();
                            }, 2300)
                        })
                        .catch(err => {

                            this.errors = err.response.data
                            let errorArr = this.errors.split("-")

                            if(errorArr.includes("source account is missing")){
                               this.sourceErr = "source account is missing"
                            }else{
                                this.sourceErr = ""
                            }
                            if(errorArr.includes("destination account is missing")){
                                this.destinationErr = "destination account is missing"
                            }else{
                                this.destinationErr = ""
                            }
                            if(errorArr.includes("must enter an amount")){
                                this.amountErr = "must enter an amount"
                            }else{
                                this.amountErr = ""
                            }
                            if(errorArr.includes("cannot enter a negative amount")){
                                this.negativeAmountErr = "cannot enter a negative amount"
                            }else{
                                this.negativeAmountErr = ""
                            }
                            if(errorArr.includes("description is missing")){
                                this.descriptionMissingErr = "description is missing"
                            }else{
                                this.descriptionMissingErr = ""
                            }
                            if(errorArr.includes("the destination account does not exist")){
                                this.destinationMissingErr = "the destination account does not exist"
                            }else{
                                this.destinationMissingErr = ""
                            }

                            console.log(errorArr)
                        })
                      
                    }
                  })
            }
        }

    }
}).mount("#app")
