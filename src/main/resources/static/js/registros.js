window.onload = function() {

    let visitaNormal = "Visita normal";

    const abrirModalConfirmacao = document.getElementById("registrar");
    const confirmarRegistro = document.getElementById("confirmar");
    const listarRegistros = document.getElementById("listar");

    let modalConfirmacao = new bootstrap.Modal(document.getElementById("confirmarRegistro"));

    abrirModalConfirmacao.addEventListener('click', function () {

        let formularioRegistro = document.getElementById("formularioRegistro");

        let titulo = formularioRegistro.titulo.value;
        let status = formularioRegistro.status.value;
        let textoStatus = "";

        if (status === "") {

            alert("Selecione um Local");
            document.getElementById("status").focus();

        } else {

            if (titulo === "") {
                titulo = visitaNormal;
            }

            switch(status) {
              case 'DIANA':
                textoStatus = "Casa da Diana"
                break;
              case 'LIAM':
                textoStatus = "Casa do Liam"
                break;
              default:
                textoStatus = "Passeio"
            }

            document.getElementById("paragrafoTitulo").innerHTML = titulo;
            document.getElementById("paragrafoStatus").innerHTML = textoStatus;

            modalConfirmacao.show();

        }

    });

    listarRegistros.addEventListener('click', function () {
        window.location.href = "/registros/listar";
    });

    confirmarRegistro.addEventListener('click', function () {
        obterLocalizacao();
    });

    function obterLocalizacao() {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(
                function(position) {

                    let latitude = position.coords.latitude;
                    let longitude = position.coords.longitude;

                    document.getElementById("latitude").value = latitude;
                    document.getElementById("longitude").value = longitude;

                    enviarFormulario();

                },
                function(error) {
                    // Erro ao obter a localização
                    switch (error.code) {
                        case error.PERMISSION_DENIED:
                            alert("Permissão para obter a localização negada pelo usuário.");
                            break;
                        case error.POSITION_UNAVAILABLE:
                            alert("Informações de localização não disponíveis.");
                            break;
                        case error.TIMEOUT:
                            alert("Tempo limite expirado ao obter a localização.");
                            break;
                        case error.UNKNOWN_ERROR:
                            alert("Erro desconhecido ao obter a localização.");
                            break;
                    }
                }
            );
        } else {
            alert("Seu navegador não suporta a API de Geolocalização.");
        }
    }

    function enviarFormulario() {
        let formulario = document.getElementById('formularioRegistro');
        formulario.submit();
    }

    function fecharModal() {
        modalConfirmacao.hide();
    }

}