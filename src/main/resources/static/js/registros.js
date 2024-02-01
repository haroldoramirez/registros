window.onload = function() {

    let visitaNormal = "Visita normal";

    const abrirModalConfirmacao = document.getElementById("registrar");
    const confirmarRegistro = document.getElementById("confirmar");

    let modalConfirmacao = new bootstrap.Modal(document.getElementById("confirmarRegistro"));

    abrirModalConfirmacao.addEventListener('click', function () {

        let formularioRegistro = document.getElementById("formularioRegistro");

        let titulo = formularioRegistro.titulo.value;
        let status = formularioRegistro.status.value;

        if (status === "") {

            alert("Selecione um Local");
            document.getElementById("status").focus();

        } else {

            if (titulo === "") {
                titulo = visitaNormal;
            }

            document.getElementById("paragrafoTitulo").innerHTML = titulo;
            document.getElementById("paragrafoStatus").innerHTML = status;

            modalConfirmacao.show();

        }

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