const { createApp } = Vue;


createApp({
    data(){
        return {
            clients: [],
            inputFirstName: '',
            inputLastName: '',
            inputEmail: '',
            responseClients: '',
            edit: null
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            axios.get('http://localhost:8080/clients')
                .then(response => {
                    this.clients = response.data._embedded.clients
                    this.responseClients = JSON.stringify(response.data, null, 4)
                })
                .catch(err => console.log(err))
        },
        addClient(){
            let newClient = {
                firstName : this.inputFirstName,
                lastName : this.inputLastName,
                email : this.inputEmail
            }
            if(newClient.firstName && newClient.lastName && newClient.email){
                this.postClient(newClient)
            }
        },
        postClient(newClient){
            axios.post('http://localhost:8080/clients', newClient)
            .then(response => {
                this.clients.push(response.data)
                this.inputFirstName = '';
                this.inputLastName = '';
                this.inputEmail = '';
                
                this.loadData();
               
            })
            .catch(err => console.log(err))

            
        },
        getClient(client, index){
            this.edit = index

            this.inputFirstName = client.firstName
            this.inputLastName = client.lastName
            this.inputEmail = client.email

            let myClient = client
            let clienId = myClient._links.client.href
            return clienId;
        },
        editClient(client){
            let btnEdit = document.getElementById(client.email)
            
            let clientEdited = {
                firstName : this.inputFirstName,
                lastName : this.inputLastName,
                email : this.inputEmail
            }
            if(clientEdited.firstName && clientEdited.lastName && clientEdited.email){
                axios.put(this.getClient(client), clientEdited)
                    .then(response => {
                        this.inputFirstName = '';
                        this.inputLastName = '';
                        this.inputEmail = '';

                        btnEdit.classList.remove('edit')
                        this.loadData()
                       
                    })
                    .catch(err => console.log(err))
            }
        },
        deleteClient(client){
          
        axios.delete(this.getClient(client))
            .then(response => {
                this.inputFirstName = '';
                this.inputLastName = '';
                this.inputEmail = '';

                this.loadData()
                
            })

        },
        cleanForm(){
            this.inputFirstName = '';
            this.inputLastName = '';
            this.inputEmail = '';
            this.edit = null
        }
    }
   

}).mount("#app")