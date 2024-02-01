window.onload = function() {

    const abrirModalConfirmacao = document.getElementById("registrar");
    const confirmarRegistro = document.getElementById("confirmar");

    let modalConfirmacao = new bootstrap.Modal(document.getElementById("confirmarRegistro"));

    abrirModalConfirmacao.addEventListener('click', function () {

        let formularioRegistro = document.getElementById("formularioRegistro");

        console.log("titulo", formularioRegistro.titulo.value);
        console.log("status", formularioRegistro.status.value);
        console.log("latitude", formularioRegistro.latitude.value);
        console.log("longitude", formularioRegistro.longitude.value);

        let titulo = formularioRegistro.titulo.value;
        let status = formularioRegistro.status.value;
        document.getElementById("paragrafoTitulo").innerHTML = titulo;
        document.getElementById("paragrafoStatus").innerHTML = status;

        modalConfirmacao.show();

    });

    confirmarRegistro.addEventListener('click', function () {

        console.log("confirmarRegistro");

        // Obtém o formulário
        let formulario = document.getElementById('formularioRegistro');

        // Realiza o submit do formulário
        formulario.submit();

    });

    function fecharModal() {

        modalConfirmacao.hide();

    }

}