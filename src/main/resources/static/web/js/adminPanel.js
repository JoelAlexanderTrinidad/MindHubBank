const { createApp } = Vue;

createApp({
    data(){
        return {
            toggle: false,
            isNavVisible: false,
            data: '',
            newNameLoan: '',
            newMaxAmountLoan: '',
            newPayLoan: '',
            newPercentageLoan: '',
            errPay: '',
            errors: '',
            loanEmpty: '',
            maxAmountEmpty: '',
            shouldBeNum: '',
            payEmpty:'',
            onlyNumbers: '',
            percentageErr: '',
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

                })
                .catch(err => console.log(err))
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
        createLoan(){

            
          Swal.fire({
            title: 'Are you sure you want to create this type of loan??',
            // text: `Are you sure you want to transfer $${this.amount} from ${this.numberAccountSource} to ${this.numberAccountDestination}?`,
            showCancelButton: true,
            confirmButtonColor: '#fe08a4',
            cancelButtonColor: '#c40e0e',
            confirmButtonText: 'Yes, give me the loan!'
          }).then((result) => {
            if (result.isConfirmed) {
                axios.post('https://localhost:8080/api/loans/admin',`loanName=${this.newNameLoan}&maxAmount=${this.newMaxAmountLoan}&payments=${this.newPayLoan}&percentage=${this.newPercentageLoan}`)
                .then(response => {
                    Swal.fire(
                        'Acquired loan!',
                        'Your loan has been acquired',
                        'success'
                      )
                      setTimeout(() => {
                        window.location.reload();
                    }, 2300)
                })
                .catch(err => {
                    console.log(err)
                    this.errors = err.response.data

                    let errorArr = this.errors.split("-")
                    console.log(errorArr)

                    if(errorArr.includes('loan name is empty')){
                        this.loanEmpty = 'loan name is invalid'
                    }else{
                        this.loanEmpty = ''
                    }
                    if(errorArr.includes('max amount is empty') || errorArr.includes('the maximum amount should only have numbers')){
                        this.maxAmountEmpty = 'max amount is invalid'
                        
                    }else{
                        this.maxAmountEmpty = ''
                    }
                    if(errorArr.includes('payments is empty') || errorArr.includes('the payments should only have numbers')){
                        this.payEmpty = 'payments is invalid'
                    }else{
                        this.payEmpty = ''
                    }
                    if(errorArr.includes('percentage is empty') || errorArr.includes('the percentage should only have numbers')){
                        this.percentageErr = 'percentage is invalid'
                    }else{
                        this.percentageErr = ''
                    }

                    


                })

            }
        })
        
        }

    }
}).mount("#app")
