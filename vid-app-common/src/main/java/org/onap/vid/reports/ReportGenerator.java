package org.onap.vid.reports;

import org.onap.vid.model.errorReport.ReportCreationParameters;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.function.BiFunction;

public interface ReportGenerator extends BiFunction<HttpServletRequest, ReportCreationParameters, Map<String, Object>>{

	boolean canGenerate(ReportCreationParameters creationParameters);

}
