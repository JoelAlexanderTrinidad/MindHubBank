const { createApp } = Vue;

createApp({
    data(){
        
        return {
            inputEmail: '',
            inputPassword: '',
            firstNameSignUp: '',
            lastNameSignUp: '',
            emailSignUp: '',
            passwordSignUp: '',
            loginError: false,
            signInError: false,
            firstName: '',
            lastName:'',
            email: '',
            pass: ''
        }
    },
    created(){
        this.loadData();

    },
    methods:{
        loadData(){
           
        },
        signIn(){
            axios.post('/api/login',`email=${this.inputEmail}&password=${this.inputPassword}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                console.log('signed in!!!')
                console.log(response)
                
                window.location.href = 'https://mindhubbank-production-6712.up.railway.app/web/views/accounts.html';

            })
            .catch(err => {
                console.log(err)
                if(err){
                    this.loginError = true
                }
            })
        },
        signUp(){
           
            axios.post('/api/clients',`firstName=${this.firstNameSignUp}&lastName=${this.lastNameSignUp}&email=${this.emailSignUp}&password=${this.passwordSignUp}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(response => {
                    console.log('registered')
                    console.log(response)
                    this.inputEmail = this.emailSignUp
                    this.inputPassword = this.passwordSignUp
                    this.signIn();
                })
                .catch(err => {
                    this.signInError = err.response.data
                    console.log(err)
                    let errorArr = this.signInError.split("-")
                    console.log(errorArr)


                    if(errorArr.includes("Missing first name")){
                        this.firstName = "Missing first name"
                    }else{
                        this.firstName = ""
                    }
                    if(errorArr.includes("Missing last name")){
                        this.lastName = "Missing last name"
                    }else{
                        this.lastName = ""
                    }
                    if(errorArr.includes("Missing email")){
                        this.email = "Missing email"
                    }else{
                        this.email = ""
                    }
                    if(errorArr.includes("Missing password")){
                        this.pass = "Missing password"
                    }else{
                        this.pass = ""
                    }
                    
                })
            }
    }
  
}).mount("#app")
