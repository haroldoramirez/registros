window.onload = function() {

    const abrirModalConfirmacao = document.getElementById("registrar");
    const confirmarRegistro = document.getElementById("confirmar");

    let modalConfirmacao = new bootstrap.Modal(document.getElementById("confirmarRegistro"));

    abrirModalConfirmacao.addEventListener('click', function () {

        modalConfirmacao.show();

    });

    confirmarRegistro.addEventListener('click', function () {

        console.log("confirmarRegistro");

        let formularioRegistro = document.getElementById("formularioRegistro");

        console.log("titulo", formularioRegistro.titulo.value);
        console.log("status", formularioRegistro.status.value);

        // Obtém o formulário
        let formulario = document.getElementById('formularioRegistro');

        // Realiza o submit do formulário
        formulario.submit();

    });

    function fecharModal() {

        modalConfirmacao.hide();

    }

}