package com.example.aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.remote.dto.RegistroRequest
import com.example.aplicacion.model.UsuarioErrores
import com.example.aplicacion.model.UsuarioUiState
import com.example.aplicacion.model.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroViewModel(private val repo: UsuarioRepository) : ViewModel() {
    private val _estado = MutableStateFlow(UsuarioUiState())

    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    fun onNombreChange(valor : String){
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }

    }

    fun onCorreoChange(valor : String){
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }
    fun onClaveChange(valor : String){
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onDireccionChange(valor : String){
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    fun onAceptarTerminosChange(valor : Boolean){
        _estado.update {it.copy(aceptaTerminos = valor, errores = it.errores.copy(aceptaTerminos = null))}
    }

    fun validarFormulario(): Boolean{
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "campo obligatorio" else null,
            correo = if (!estadoActual.correo.contains("@")) "Correo Invalido" else null,
            clave = if (estadoActual.clave.length < 6) "Debe tener al menos 6 caracteres" else null,
            direccion = if (estadoActual.direccion.isBlank()) "Campo Obligatorio" else null,
            aceptaTerminos = if (!estadoActual.aceptaTerminos) "Debes aceptar los términos" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.direccion,
            errores.aceptaTerminos
        ).isNotEmpty()

        _estado.update {it.copy(errores = errores)}

        return !hayErrores
    }
    fun registrarUsuario(){
        if(validarFormulario()){
            val request = RegistroRequest(
                nombre = estado.value.nombre,
                email = estado.value.correo,
                password = estado.value.clave,
                direccion = estado.value.direccion
            )

            viewModelScope.launch {
                try{
                    val response = repo.registrarUsuarioRemoto(request)
                    if(response.isSuccessful && response.body() != null){
                        val token = response.body()!!.token
                        _estado.update {it.copy(registroExitoso = true, errores = UsuarioErrores())}
                    }else{
                        val errorMsg = "El correo ya esta registrado o hubo un error."
                        _estado.update {it.copy(errores = it.errores.copy(correo = errorMsg))}

                    }
                }catch (e: Exception){
                    _estado.update { it.copy(errores = it.errores.copy(nombre = "No se pudo conectar al servidor. Intenta de nuevo.")) }
                }
            }
        }
    }
    fun onNavegacionRealizada(){
        _estado.update { it.copy(registroExitoso = false) }
    }

}
