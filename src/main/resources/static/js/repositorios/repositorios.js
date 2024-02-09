console.log("repositorios");

let filterFileList = [];

//Montar o objeto para realizar o filtro
const filtroDocumento = new Object();
filtroDocumento.idCentral = "";
filtroDocumento.idSingular = "";
filtroDocumento.tipoDocumento = "";

$("#centrais").change(function() {
	let centralSelecionada = $("#centrais option:selected").val();
	if (centralSelecionada) {
		$.ajax({
			url: $portletAjaxURLCarregarTodasSingularesSCI,
			type: "GET",
			datatype: "json",
			data: {
				idCentral: centralSelecionada,
			},
			beforeSend: function() {
				$("#loader").show();
			},
			success: function(data) {
				if (!data) {
					throw new Error("Estrutura de dados inválida enviada pelo servidor.");
				}
				let retorno = JSON.parse(data);
				let listaSingulares = retorno.objeto;
		       	createSelectSingulares(listaSingulares);
			},
			error: function(xhr) {
				console.log("Ocorreu um erro ao pesquisar a lista de PAs: " + xhr);
			},
			complete: function() {
				$("#loader").hide();
			},
		});
	}
});

function createSelectSingulares(lista) {
	let options = "";
	$("#cooperativas").attr("disabled", false);
	$("#cooperativas").html("");
	$("#cooperativas").append('<option value="">Selecione a Cooperativa</option> \n');
	$("#cooperativas").append('<option value="TODAS">Todas as Cooperativas</option> \n');
	$.each(lista, function(indice) {
		options += '<option value="'
			+ lista[indice].idCooperativa + '">' + lista[indice].idCooperativa + ' - ' +lista[indice].nomeCooperativa + '</option> \n';
	});
	$("#cooperativas").append(options);
}

//Validadores dos campos
$("#filtroDocumentoForm").validate({
	rules : {
		centrais: "required",
		cooperativas: "required",
		tipoDocumentos: "required"
	},
	messages: {
		centrais: "Selecione uma Central",
		cooperativas: "Selecione uma Cooperativa",
		tipoDocumentos: "Selecione um Tipo de Documento",
	},
  	submitHandler: function() {
		filtroDocumento.idCentral =  $("#centrais option:selected").val();
		filtroDocumento.idSingular = $("#cooperativas option:selected").val();
		filtroDocumento.tipoDocumento = $("#tipoDocumentos option:selected").val();
		$.ajax({
			url: $portletAjaxURLBuscarArquivos,
			type: "GET",
			cache: false,
			datatype: "json",
			data: {
				filtroDocumento: JSON.stringify(filtroDocumento),
			},
			beforeSend: function() {
				if (!$("#filtroDocumentoForm").valid()) {
					return false;
				}
				$("#loader").show();
				initResultTable();
				hideResultTable();
			},
			success: function(data) {
				try {
					if (!data) {
						throw new Error("Estrutura de dados inválida enviada pelo servidor.");
					}
					let content = JSON.parse(data);
					filterFileList = content.objeto;
					if (Array.isArray(filterFileList) && filterFileList.length) {
						createResultTable(filterFileList);
						showResultTable();
					} else {
						setTimeout(function(){ alert("Nenhum resultado encontrado com os filtros selecionados"); }, 300);
					}
				} catch(e) {
					initResultTable();
					hideResultTable();
					$("#mensagem-listar").find(".message").text("Ocorreu um erro ao pesquisar a lista de arquivos, por favor tente novamente!");
					$("#mensagem-listar").show();
					console.log("Ocorreu um erro ao pesquisar a lista de arquivos erro " + e);
				}

			},
			error: function(xhr) {
				initResultTable();
				hideResultTable();
				$("#mensagem-listar").find(".message").text("Ocorreu um erro ao pesquisar a lista de arquivos, por favor tente novamente!");
				$("#mensagem-listar").show();
				console.log("Ocorreu um erro ao pesquisar a lista de arquivos erro: " + xhr);
			},
			complete: function() {
				$("#loader").hide();
			},
		});

  	},

});

function showResultTable() {
	$("#arquivosRepositorio").show();
}

function hideResultTable() {
	$("#arquivosRepositorio").hide();
}

function initResultTable() {
	$("#arquivosRepositorio table tbody").empty();
}

function createResultTable(lista) {
	$.each(lista, function(indice) {
		$("#arquivosRepositorio table tbody").append(
			"<tr>" +
				"<td>" +
					lista[indice].dataAlteracao +
				"</td>" +
				"<td>" +
					lista[indice].nome +
				"</td>" +
				"<td>" +
					lista[indice].tipoDocumento +
				"</td>" +
				"<td>" +
					lista[indice].nomeCentral +
				"</td>" +
				"<td>" +
					lista[indice].cooperativa +
				"</td>" +
				"<td>" +
					"<a href='" + lista[indice].urlDownload + "' title='Clique para efetuar o download do documento' download>" +
						"<svg width='16' height='16' viewBox='0 0 16 16' fill='none' xmlns='http://www.w3.org/2000/svg'>" +
							"<path d='M4.79995 1.59998C3.91745 1.59998 3.19995 2.31748 3.19995 3.19998V12.8C3.19995 13.6825 3.91745 14.4 4.79995 14.4H11.2C12.0825 14.4 12.8 13.6825 12.8 12.8V5.59998H9.59995C9.15745 5.59998 8.79995 5.24248 8.79995 4.79998V1.59998H4.79995ZM9.59995 1.59998V4.79998H12.8L9.59995 1.59998ZM8.59995 7.39998V9.95248L9.37495 9.17748C9.60995 8.94248 9.98995 8.94248 10.2225 9.17748C10.455 9.41248 10.4575 9.79248 10.2225 10.025L8.42245 11.825C8.18745 12.06 7.80745 12.06 7.57495 11.825L5.77495 10.025C5.53995 9.78998 5.53995 9.40998 5.77495 9.17748C6.00995 8.94498 6.38995 8.94248 6.62245 9.17748L7.39745 9.95248V7.39998C7.39745 7.06748 7.66495 6.79998 7.99745 6.79998C8.32995 6.79998 8.59745 7.06748 8.59745 7.39998H8.59995Z' fill='#00A091'/>" +
						"</svg>" +
						"<span>Download</span>"+
					"</a>" +
				"</td>" +
			"</tr>"
		);
	});
}