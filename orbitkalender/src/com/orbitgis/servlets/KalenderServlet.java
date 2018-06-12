package com.orbitgis.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.orbitgis.entities.Gebruiker;
import com.orbitgis.entities.Kalender;
import com.orbitgis.entities.KalenderEntry;
import com.orbitgis.repository.KalenderRepository;

/**
 * Servlet implementation class KalenderServlet
 */
@WebServlet("/index.htm")
public class KalenderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/kalender.jsp";
	private final transient KalenderRepository kalenderRepository = new KalenderRepository();
	
	@Resource(name = KalenderRepository.JNDI_NAME)
	void setDataSource(DataSource dataSource) {
		kalenderRepository.setDataSource(dataSource);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Kalender kalenderUitDatabase = kalenderRepository.getKalenderUitDatabaseViaDataSource();
		Map<LocalDate, Set<KalenderEntry>> kalenderMap = kalenderUitDatabase.getKalenderMap();
		List<Gebruiker> gebruikers = kalenderUitDatabase.getGebruikers();
		request.setAttribute("kalendermap", kalenderMap);
		request.setAttribute("gebruikers", gebruikers);
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

}
