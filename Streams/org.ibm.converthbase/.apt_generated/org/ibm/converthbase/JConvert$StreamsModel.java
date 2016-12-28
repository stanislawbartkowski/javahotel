package org.ibm.converthbase;
import com.ibm.streams.operator.metrics.Metric.Kind;
import com.ibm.streams.operator.model.InputPortSet.WindowMode;
import com.ibm.streams.operator.model.InputPortSet.WindowPunctuationInputMode;
import com.ibm.streams.operator.model.OutputPortSet.WindowPunctuationOutputMode;

@com.ibm.streams.operator.model.Libraries(value={"opt/*"})
@com.ibm.streams.operator.model.PrimitiveOperator(name="JConvert", namespace="org.ibm.converthbase", description="Java Operator JConvert")
@com.ibm.streams.operator.model.InputPorts(value={@com.ibm.streams.operator.model.InputPortSet(description="Port that ingests tuples", cardinality=1, optional=false, windowingMode=WindowMode.NonWindowed, windowPunctuationInputMode=WindowPunctuationInputMode.Oblivious), @com.ibm.streams.operator.model.InputPortSet(description="Optional input ports", optional=true, windowingMode=WindowMode.NonWindowed, windowPunctuationInputMode=WindowPunctuationInputMode.Oblivious)})
@com.ibm.streams.operator.model.OutputPorts(value={@com.ibm.streams.operator.model.OutputPortSet(description="Port that produces tuples", cardinality=1, optional=false, windowPunctuationOutputMode=WindowPunctuationOutputMode.Generating), @com.ibm.streams.operator.model.OutputPortSet(description="Optional output ports", optional=true, windowPunctuationOutputMode=WindowPunctuationOutputMode.Generating)})
@com.ibm.streams.operator.internal.model.ShadowClass("org.ibm.converthbase.JConvert")
@javax.annotation.Generated("com.ibm.streams.operator.internal.model.processors.ShadowClassGenerator")
public class JConvert$StreamsModel extends com.ibm.streams.operator.AbstractOperator
 {
}