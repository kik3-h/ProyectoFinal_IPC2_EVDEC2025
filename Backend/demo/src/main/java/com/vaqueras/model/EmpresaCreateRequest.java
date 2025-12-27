package com.vaqueras.model;

import java.time.LocalDate;

public class EmpresaCreateRequest {
    private EmpresaData empresa;
    private AdminEmpresaUser admin;  // el usuario EMPRESA principal
    private String cargo;            // opcional (default: ADMIN)

    public EmpresaCreateRequest() {}

    public EmpresaData getEmpresa() { return empresa; }
    public void setEmpresa(EmpresaData empresa) { this.empresa = empresa; }

    public AdminEmpresaUser getAdmin() { return admin; }
    public void setAdmin(AdminEmpresaUser admin) { this.admin = admin; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    // --- Clases internas DTO ---
    public static class EmpresaData {
        private String nombreEmpresa;
        private String email;
        private String descripcion;

        public EmpresaData() {}

        public String getNombreEmpresa() { return nombreEmpresa; }
        public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }

    public static class AdminEmpresaUser {
        private String nickname;
        private String email;
        private String password;
        private String telefono;           // en tu DB no es NOT NULL
        private LocalDate fechaNacimiento; // en tu DB sí es NOT NULL
        private String pais; 
        private String cargo; // para usuario_empresa.cargo (DEFAULT 'Administrador_Empresa')              // en tu DB no es NOT NULL

        public AdminEmpresaUser() {}

        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }

        public LocalDate getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

        public String getPais() { return pais; }
        public void setPais(String pais) { this.pais = pais; }

        public String getCargo() { return cargo; }
        public void setCargo(String cargo) { this.cargo = cargo; }
    }
}
