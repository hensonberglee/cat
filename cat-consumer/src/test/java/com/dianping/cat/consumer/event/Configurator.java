package com.dianping.cat.consumer.event;

import java.util.ArrayList;
import java.util.List;

import org.unidal.lookup.annotation.Inject;
import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

import com.dianping.cat.Constants;
import com.dianping.cat.consumer.MockReportManager;
import com.dianping.cat.consumer.event.model.entity.EventReport;
import com.dianping.cat.report.ReportDelegate;
import com.dianping.cat.report.ReportManager;

public class Configurator extends AbstractResourceConfigurator {

	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new Configurator());
	}

	protected Class<?> getTestClass() {
		return EventAnalyzerTest.class;
	}

	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();
		final String ID = EventAnalyzer.ID;

		all.add(C(ReportManager.class, ID, MockEventReportManager.class)//
		      .req(ReportDelegate.class, ID));
		all.add(C(ReportDelegate.class, ID, ExtendedEventDelegate.class));

		return all;
	}
	
	public static class ExtendedEventDelegate extends EventDelegate{
	}
	
	public static class MockEventReportManager extends MockReportManager<EventReport> {
		private EventReport m_report;

		@Inject
		private ReportDelegate<EventReport> m_delegate;

		@Override
		public EventReport getHourlyReport(long startTime, String domain, boolean createIfNotExist) {
			if (m_report == null) {
				m_report = (EventReport) m_delegate.makeReport(domain, startTime, Constants.HOUR);
			}

			return m_report;
		}

		@Override
      public void destory() {
      }
	}
	
}
