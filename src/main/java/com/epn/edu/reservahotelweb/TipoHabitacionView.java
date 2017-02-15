/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.reservahotelweb;

/**
 *
 * @author Daniela Ramos
 */
import com.epn.edu.reservahotel.entidades.Habitacion;
import com.epn.edu.reservahotel.entidades.Reserva;
import com.epn.edu.reservahotel.entidades.TipoHabitacion;
import com.epn.edu.reservahotel.entidades.ReHabitacion;
import com.epn.edu.reservahotel.entidades.ReHabitacionPK;
import com.epn.edu.reservahotel.jpacontroller.HabitacionJpaController;
import com.epn.edu.reservahotel.jpacontroller.ReHabitacionJpaController;
import com.epn.edu.reservahotel.jpacontroller.TipoHabitacionJpaController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.primefaces.event.SelectEvent;

@ViewScoped
@ManagedBean(name = "tipoHabitacionView")
public class TipoHabitacionView implements Serializable {
    
    private List<TipoHabitacion> listTipoHbitacion;
    private List<Habitacion> lstHabitacion;
    private String selectedTipoHabitacion;
    private Date selectedFechaInicio;
    private String fechasSeleccionadas;
    
    public String getFechasSeleccionadas() {
        return fechasSeleccionadas;
    }
    
    public void setFechasSeleccionadas(String fechasSeleccionadas) {
        this.fechasSeleccionadas = fechasSeleccionadas;
    }
    private Date selectedFechaFin;
    private Date fechaActual;
    private String cabeceraTabla;
    private List<Habitacion> selectedHabitaciones;
    private SimpleDateFormat dateFormat;
    private BigDecimal costoTotal;
    private List<ReHabitacion> listReHabitacion;
    
    public BigDecimal getCostoTotal() {
        return costoTotal;
    }
    
    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }
    List<Integer> lstIdHabitacionesDisponibles;
    TipoHabitacionJpaController tipoHabitacionJpaController;
    ReHabitacionJpaController reHabitacionJpaController;
    HabitacionJpaController habitacionJpaController;
    
    @PersistenceUnit(unitName = "com.epn.edu_ReservaHotelWeb_war_1.0-SNAPSHOTPU")
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction utx;
    
    public TipoHabitacionView() {
    }
    
    @PostConstruct
    public void init() {
        //llenar cosas al iniciar

        tipoHabitacionJpaController = new TipoHabitacionJpaController(utx, emf);
        habitacionJpaController = new HabitacionJpaController(utx, emf);
        reHabitacionJpaController = new ReHabitacionJpaController(utx, emf);
        listTipoHbitacion = tipoHabitacionJpaController.findTipoHabitacionEntities();
        fechaActual = Calendar.getInstance().getTime();
//        lstIdHabitacionesDisponibles = reHabitacionJpaController.findIdHabitacionByFecha(fechaActual);
//        Integer[] ids = new Integer[lstIdHabitacionesDisponibles.size()];
//        for (Integer idHabitacionesDisponible : lstIdHabitacionesDisponibles) {
//            ids[lstIdHabitacionesDisponibles.indexOf(idHabitacionesDisponible)] = idHabitacionesDisponible;
//        }
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        cabeceraTabla = "Habitaciones disponibles para el " + dateFormat.format(fechaActual);
        lstHabitacion = habitacionJpaController.findHabitacionesDisponiblesUnDia(fechaActual);

        //     System.out.println("seleccionado:"+ selectedTipoHabitacion.getIdTipoHabitacion());
    }
    
    public void onDateSelect(SelectEvent event) {
        onChangeTipoHabitacionSelect();
    }
    
    public void onChangeTipoHabitacionSelect() {
        // System.out.println("entro, tipo habitacionseleccionada:"+selectedTipoHabitacion+" Inicio: "+selectedFechaInicio+" FIn: "+selectedFechaFin+"evento :"+event.getObject().toString());
        if (selectedTipoHabitacion != null && selectedFechaInicio == null && selectedFechaFin == null) {
            System.out.println("selec" + selectedTipoHabitacion);
            lstHabitacion = habitacionJpaController.findHabitacionbyTypeHabitacionId(Integer.parseInt(selectedTipoHabitacion));
            fechasSeleccionadas = dateFormat.format(fechaActual);
            if (!lstHabitacion.isEmpty()) {
                cabeceraTabla = "Habitaciones " + lstHabitacion.get(0).getIdTipoHabitacion().getDescripcion() + " disponibles para el " + dateFormat.format(fechaActual);
            } else {
                cabeceraTabla = "No existen habitaciones disponibles para estos filtros";
            }
            System.out.println("Tamaño lista " + lstHabitacion.size());
        } else if (selectedTipoHabitacion != null && selectedFechaInicio != null && selectedFechaFin == null) {
            System.out.println("entro");
            lstHabitacion = habitacionJpaController.findHabitacionesDisponiblesUnDiaAndTipoHabitacion(selectedFechaInicio, Integer.parseInt(selectedTipoHabitacion));
            fechasSeleccionadas = dateFormat.format(selectedFechaInicio);
            if (!lstHabitacion.isEmpty()) {
                cabeceraTabla = "Habitaciones " + lstHabitacion.get(0).getIdTipoHabitacion().getDescripcion() + " disponibles para el " + dateFormat.format(selectedFechaInicio);
            } else {
                cabeceraTabla = "No existen habitaciones disponibles para estos filtros";
            }
            System.out.println("Tamaño lista " + lstHabitacion.size());
        } else if (selectedTipoHabitacion != null && selectedFechaInicio != null && selectedFechaFin != null) {
            lstHabitacion = habitacionJpaController.findHabitacionesDisponiblesRangoDiasAndTipoHabitacion(selectedFechaInicio, selectedFechaFin, Integer.parseInt(selectedTipoHabitacion));
            fechasSeleccionadas = dateFormat.format(selectedFechaInicio) + " al " + dateFormat.format(selectedFechaFin);
            if (!lstHabitacion.isEmpty()) {
                cabeceraTabla = "Habitaciones " + lstHabitacion.get(0).getIdTipoHabitacion().getDescripcion() + " disponibles del " + dateFormat.format(selectedFechaInicio) + " al " + dateFormat.format(selectedFechaFin);
            } else {
                cabeceraTabla = "No existen habitaciones disponibles para estos filtros";
            }
            System.out.println("Tamaño lista " + lstHabitacion.size());
        } else if (selectedTipoHabitacion == null && selectedFechaInicio != null && selectedFechaFin != null) {
            System.out.println("fecha inicio y fin");
            lstHabitacion = habitacionJpaController.findHabitacionesDisponiblesRangoDias(selectedFechaInicio, selectedFechaFin);
            fechasSeleccionadas = dateFormat.format(selectedFechaInicio) + " al " + dateFormat.format(selectedFechaFin);
            if (!lstHabitacion.isEmpty()) {
                cabeceraTabla = "Habitaciones disponibles del " + dateFormat.format(selectedFechaInicio) + " al " + dateFormat.format(selectedFechaFin);
            } else {
                cabeceraTabla = "No existen habitaciones disponibles para estos filtros";
            }
            System.out.println("Tamaño lista " + lstHabitacion.size());
            
        } else if (selectedTipoHabitacion == null && selectedFechaInicio != null && selectedFechaFin == null) {
            System.out.println("solo inicio para :" + selectedFechaInicio);
            lstHabitacion = habitacionJpaController.findHabitacionesDisponiblesUnDia(selectedFechaInicio);
            fechasSeleccionadas = dateFormat.format(selectedFechaInicio);
            if (!lstHabitacion.isEmpty()) {
                cabeceraTabla = "Habitaciones disponibles para el " + dateFormat.format(selectedFechaInicio);
                
            } else {
                cabeceraTabla = "No existen habitaciones disponibles para estos filtros";
            }
            System.out.println("Tamaño lista " + lstHabitacion.size());
        } else {
            lstHabitacion = habitacionJpaController.findHabitacionesDisponiblesUnDia(fechaActual);
            fechasSeleccionadas = dateFormat.format(fechaActual);
        }
    }
    
    public Date fechaMaximaReserva() {
        
        Calendar calendar = Calendar.getInstance();
        
        calendar.setTime(fechaActual); // Configuramos la fecha que se recibe

        calendar.add(Calendar.YEAR, 1);  // numero de días a añadir, o restar en caso de días<0

        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos

    }
    
    public Date getFechaActual() {
        return fechaActual;
    }
    
    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }
    
    public String getCabeceraTabla() {
        return cabeceraTabla;
    }
    
    public void setCabeceraTabla(String cabeceraTabla) {
        this.cabeceraTabla = cabeceraTabla;
    }
    
    public void setSelectedHabitaciones(List<Habitacion> selectedHabitaciones) {
        costoTotal = BigDecimal.ZERO;
        for (Habitacion habitacion : selectedHabitaciones) {
            BigDecimal valorHabitacion = habitacion.getIdTipoHabitacion().getCosto();
            BigDecimal valorExtras = habitacion.getIdExtraHabitacion().getCosto();
            costoTotal = costoTotal.add(valorExtras).add(valorHabitacion);
        }
        this.selectedHabitaciones = selectedHabitaciones;
    }
    
    public void registroRerservaHabitacion(Date fechaInicio, Date fechaFin, List<Habitacion> listSelectHabotaciones) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(fechaFin);
        Reserva reserva;
        ReHabitacion reHabitacion;
        List<Reserva> listReserva = null;
        reserva = new Reserva();
        ReHabitacionPK reHabitacionPk = null;
        
        for (Habitacion Habitacion : listSelectHabotaciones) {
            c1.setTime(fechaInicio);
            
            while (!c1.after(c2)) {
                reHabitacionPk.setFechaReservaHabitacion(c1.getTime());
                reHabitacionPk.setIdHabitacion(Habitacion.getIdHabitacion());
                reHabitacion = new ReHabitacion();
                reHabitacion.setReHabitacionPK(reHabitacionPk);
                reHabitacion.setHabitacion(Habitacion);
                listReHabitacion.add(reHabitacion);
                c1.add(Calendar.DAY_OF_MONTH, 1);
            }
            
        }
        reserva.setReHabitacionList(listReHabitacion);
        
    }
    
    public List<Habitacion> getSelectedHabitaciones() {
        return selectedHabitaciones;
    }
    
    public List<Habitacion> getLstHabitacion() {
        return lstHabitacion;
    }
    
    public void setLstHabitacion(List<Habitacion> lstHabitacion) {
        this.lstHabitacion = lstHabitacion;
    }
    
    public String getSelectedTipoHabitacion() {
        return selectedTipoHabitacion;
    }
    
    public Date getSelectedFechaFin() {
        return selectedFechaFin;
    }
    
    public void setSelectedFechaFin(Date selectedFechaFin) {
        this.selectedFechaFin = selectedFechaFin;
    }
    
    public Date getSelectedFechaInicio() {
        return selectedFechaInicio;
    }
    
    public void setSelectedFechaInicio(Date selectedFechaInicio) {
        this.selectedFechaInicio = selectedFechaInicio;
    }
    
    public void setSelectedTipoHabitacion(String selectedTipoHabitacion) {
        this.selectedTipoHabitacion = selectedTipoHabitacion;
    }
    
    public List<TipoHabitacion> getListTipoHbitacion() {
        return listTipoHbitacion;
    }
    
    public void setListTipoHbitacion(List<TipoHabitacion> listTipoHbitacion) {
        this.listTipoHbitacion = listTipoHbitacion;
    }
    
}
