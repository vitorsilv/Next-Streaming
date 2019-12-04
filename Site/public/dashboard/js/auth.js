function verificarSessao(){
    if(sessionStorage.getItem('id')==null){
        window.location.href ="../login/Login.html"
    }
}

function logout(){
    sessionStorage.removeItem('id');
    sessionStorage.removeItem('nomeUsuario')
    window.location.href ="../login/Login.html"
}