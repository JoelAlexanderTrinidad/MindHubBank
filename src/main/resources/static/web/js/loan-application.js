const { createApp } = Vue;

createApp({
    data(){
        return {
            toggle: false,
            isNavVisible: false,
            data: '',
            errors: '',
            loans: '',
            loan:'',

            loanErr: '',
            missingAmountErr: '',
            amountLess1Err: '',
            missingPayment: '',
            notCero: '',
            missingDestAcc: '',
            exceededMaxAmount: '',
            destAccNotFound: '',
            dontHaveAcc: '',
            payments:'',
            fields:'',

            payment:'',
            accounts: [],
            amount:'',
            destination:'',
            mortgage:'',
            personal:'',
            automotive:'',
            mortgagePayments: [],
            personalPayments: [],
            automotivePayments: [],
            mortgageMaxAmount: '',
            personalMaxAmount: '',
            automotiveMaxAmount: '',
            creditType: 'Select a credit type...',
            selectedPayment:'Select available quotas...',
            totalToPay: '',
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            axios.get('https://mindhubbank-8dkk.onrender.com/api/clients/current')
                .then(response => {
                    this.data = response.data;
                    this.accounts = response.data.accounts.filter(account => account.eliminated == false)

                })
                .catch(err => console.log(err))

            axios.get('https://mindhubbank-8dkk.onrender.com/api/loans')
                .then(response => {
                    this.loans = response.data
                    console.log(this.loans)
                    

                                                                                                    // uso reduce para no devolver un array de arrays (en el caso de map, porque el filter anterior ya devuelvió uno)
                                                                                                    // accumulador es el [] el segundo parámetro de reduce
                                                                                                    // entonces a eso le concatenamos (concat) el valor actual.payments
                                                                                                    // y así hasta completar el recorrido, dándonos un solo array
                    this.mortgagePayments = response.data.filter(credit => credit.name == 'mortgage').reduce((accumulator, currentValue) => accumulator.concat(currentValue.payments), []);
                    this.personalPayments = response.data.filter(credit => credit.name == 'personal').reduce((accumulator, currentValue) => accumulator.concat(currentValue.payments), []);
                    this.automotivePayments = response.data.filter(credit => credit.name == 'automotive').reduce((accumulator, currentValue) => accumulator.concat(currentValue.payments), []);

                    this.mortgageMaxAmount = response.data.filter(credit => credit.name == 'mortgage').map(credit => credit.maxAmount)
                    this.personalMaxAmount = response.data.filter(credit => credit.name == 'personal').map(credit => credit.maxAmount)
                    this.automotiveMaxAmount = response.data.filter(credit => credit.name == 'automotive').map(credit => credit.maxAmount)

                })
                .catch(err => console.log(err))
        },
        toggleBtn(){
            this.toggle = !this.toggle;
        },
        burgerMenu(){
            this.toggle = !this.toggle
            console.log(this.toggle)
        },
        signOut(){
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = 'https://mindhubbank-8dkk.onrender.com/web/views/index.html';
        },
        createLoan(){
          let loan = this.loans.find(loan => loan.name == this.creditType)
          if(loan != undefined){
            this.loan = loan.id
          }else{
            this.loan = null
          }

          Swal.fire({
            title: 'Are you sure?',
            // text: `Are you sure you want to transfer $${this.amount} from ${this.numberAccountSource} to ${this.numberAccountDestination}?`,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#fe08a4',
            cancelButtonColor: '#c40e0e',
            confirmButtonText: 'Yes, give me the loan!'
          }).then((result) => {
            if (result.isConfirmed) {
              axios.post('/api/loans', { 
                loanID: this.loan, 
                amount: this.amount, 
                payments: this.selectedPayment, 
                destinationAccount: this.destination
              })
                .then(response => {

                    Swal.fire(
                        'Acquired loan!',
                        'Your loan has been acquired',
                        'success'
                      )
                      setTimeout(() => {
                        window.location.href = 'https://mindhubbank-8dkk.onrender.com/web/views/accounts.html';
                    }, 2300)
                })
                .catch(err => {
                    
                    this.errors = err.response.data

                    console.log(typeof this.errors)
                    if(typeof this.errors == 'object'){
                      this.fields = 'you must complete the fields'
                    }
                    if(typeof this.errors == 'string'){
                      this.fields = ''
                    }

                    if(typeof this.errors == 'string'){
                      let errorArr =  this.errors.split("-") 
                      if(errorArr.includes("missing amount")){
                        this.missingAmountErr = "missing amount"
                    }else{
                        this.missingAmountErr = ""
                    }
                    if(errorArr.includes("the amount cannot be less than 1")){
                        this.amountLess1Err = "the amount cannot be less than 1"
                    }else{
                        this.amountLess1Err = ""
                    }
                    if(errorArr.includes("missing payment")){
                        this.missingPayment = "missing payment"
                    }else{
                        this.missingPayment = ""
                    }
                    if(errorArr.includes("the payment cannot be 0")){
                        this.notCero = "the payment cannot be 0"
                    }else{
                        this.notCero = ""
                    }
                    if(errorArr.includes("missing destination account")){
                        this.missingDestAcc = "missing destination account"
                    }else{
                        this.missingDestAcc = ""
                    }
                    if(errorArr.includes("you exceeded the maximum amount")){
                       this.exceededMaxAmount = "you exceeded the maximum amount"
                   }else{
                       this.exceededMaxAmount = ""
                   }
                   if(errorArr.includes("destination account not found")){
                       this.destAccNotFound = "destination account not found"
                   }else{
                       this.destAccNotFound = ""
                   }
                   if(errorArr.includes("you don't have that account")){
                       this.dontHaveAcc = "you don't have that account"
                   }else{
                       this.dontHaveAcc = ""
                   }
                    }
                    

                    
                })
              
            }
          })
   
        }
    
    },
    computed: {
     showTotal(){
        if(this.creditType == 'personal'){
          return (this.amount * 0.3) + this.amount
        }
        if(this.creditType == 'mortgage'){
          return (this.amount * 0.2) + this.amount
        }
        if(this.creditType == 'automotive'){
          return (this.amount * 0.15) + this.amount
        }
      }
    }
}).mount("#app")
