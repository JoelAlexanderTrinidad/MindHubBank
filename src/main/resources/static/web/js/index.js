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
            axios.post('https://mindhubbank-8dkk.onrender.com/api/login',`email=${this.inputEmail}&password=${this.inputPassword}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                console.log('signed in!!!')
                console.log(response)
                
                window.location.href = 'https://mindhubbank-8dkk.onrender.com/web/views/accounts.html';

            })
            .catch(err => {
                console.log(err)
                if(err){
                    this.loginError = true
                }
            })
        },
        signUp(){
           
            axios.post('https://mindhubbank-8dkk.onrender.com/api/clients',`firstName=${this.firstNameSignUp}&lastName=${this.lastNameSignUp}&password=${this.passwordSignUp}&email=${this.emailSignUp}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(response => {
                    console.log('registered')
                    console.log(response)
                    this.inputEmail = this.emailSignUp
                    this.inputPassword = this.passwordSignUp
                    this.signIn();
                })
                .catch(err => {
                    console.error('Error during sign-up:', err);
                    this.signInError = err.response.data;
                    let errorArr = this.signInError.split("-");
                    console.log('Error array:', errorArr);
                    

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
