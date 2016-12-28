/* Generated by Streams Studio: 27 grudnia 2016 14:34:43 CET */
package org.ibm.converthbase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ibmstreams.bigsql.mapping.BIGSQLTYPE;
import org.ibmstreams.bigsql.mapping.HVal;

import com.ibm.streams.operator.AbstractOperator;
import com.ibm.streams.operator.Attribute;
import com.ibm.streams.operator.OperatorContext;
import com.ibm.streams.operator.OperatorContext.ContextCheck;
import com.ibm.streams.operator.OutputTuple;
import com.ibm.streams.operator.StreamSchema;
import com.ibm.streams.operator.StreamingData.Punctuation;
import com.ibm.streams.operator.StreamingInput;
import com.ibm.streams.operator.StreamingOutput;
import com.ibm.streams.operator.Tuple;
import com.ibm.streams.operator.Type;
import com.ibm.streams.operator.compile.OperatorContextChecker;
import com.ibm.streams.operator.meta.TupleType;
import com.ibm.streams.operator.model.InputPortSet;
import com.ibm.streams.operator.model.InputPortSet.WindowMode;
import com.ibm.streams.operator.model.InputPortSet.WindowPunctuationInputMode;
import com.ibm.streams.operator.model.InputPorts;
import com.ibm.streams.operator.model.Libraries;
import com.ibm.streams.operator.model.OutputPortSet;
import com.ibm.streams.operator.model.OutputPortSet.WindowPunctuationOutputMode;
import com.ibm.streams.operator.model.OutputPorts;
import com.ibm.streams.operator.model.PrimitiveOperator;
import com.ibm.streams.operator.types.Blob;
import com.ibm.streams.operator.types.Timestamp;
import com.ibm.streams.operator.types.ValueFactory;

/**
 * Class for an operator that receives a tuple and then optionally submits a
 * tuple. This pattern supports one or more input streams and one or more output
 * streams.
 * <P>
 * The following event methods from the Operator interface can be called:
 * </p>
 * <ul>
 * <li><code>initialize()</code> to perform operator initialization</li>
 * <li>allPortsReady() notification indicates the operator's ports are ready to
 * process and submit tuples</li>
 * <li>process() handles a tuple arriving on an input port
 * <li>processPuncuation() handles a punctuation mark arriving on an input port
 * <li>shutdown() to shutdown the operator. A shutdown request may occur at any
 * time, such as a request to stop a PE or cancel a job. Thus the shutdown() may
 * occur while the operator is processing tuples, punctuation marks, or even
 * during port ready notification.</li>
 * </ul>
 * <p>
 * With the exception of operator initialization, all the other events may occur
 * concurrently with each other, which lead to these methods being called
 * concurrently by different threads.
 * </p>
 */
@Libraries({ "opt/*" })
@PrimitiveOperator(name = "JConvert", namespace = "org.ibm.converthbase", description = "Java Operator JConvert")
@InputPorts({
		@InputPortSet(description = "Port that ingests tuples", cardinality = 1, optional = false, windowingMode = WindowMode.NonWindowed, windowPunctuationInputMode = WindowPunctuationInputMode.Oblivious),
		@InputPortSet(description = "Optional input ports", optional = true, windowingMode = WindowMode.NonWindowed, windowPunctuationInputMode = WindowPunctuationInputMode.Oblivious) })
@OutputPorts({
		@OutputPortSet(description = "Port that produces tuples", cardinality = 1, optional = false, windowPunctuationOutputMode = WindowPunctuationOutputMode.Generating),
		@OutputPortSet(description = "Optional output ports", optional = true, windowPunctuationOutputMode = WindowPunctuationOutputMode.Generating) })
public class JConvert extends AbstractOperator {

	private static void echo(String s) {
		System.out.println(s);
		Logger.getLogger(JConvert.class.getName()).log(Level.DEBUG, s);
	}

	private static Type.MetaType aType[] = new Type.MetaType[] { Type.MetaType.BOOLEAN, Type.MetaType.INT8,
			Type.MetaType.INT16, Type.MetaType.INT32, Type.MetaType.INT64, Type.MetaType.RSTRING, Type.MetaType.TUPLE,
			Type.MetaType.UINT8, Type.MetaType.UINT16, Type.MetaType.UINT32, Type.MetaType.UINT64,
			Type.MetaType.FLOAT32, Type.MetaType.FLOAT64, Type.MetaType.TIMESTAMP, Type.MetaType.DECIMAL32,
			Type.MetaType.DECIMAL64, Type.MetaType.DECIMAL128 };

	private static String getDesc(String t, Attribute a) {
		return t + ", attribute " + a.getName() + ", type " + a.getType().getMetaType().name();
	}

	private static void verifyType(String t1, String t2, Iterator<Attribute> i, Type.MetaType[] aType) {
		while (i.hasNext()) {
			Attribute a = i.next();
			boolean found = false;
			for (Type.MetaType t : aType) {
				if (t == a.getType().getMetaType()) {
					found = true;
					break;
				}
			}
			if (!found) {
				String al = null;
				for (Type.MetaType t : aType)
					if (al == null)
						al = t.name();
					else
						al += ", " + t.name();

				String m = getDesc(t1, a) + ". Not supported type in " + t2 + " stream. Only " + al + " "
						+ (aType.length == 1 ? "is" : "are") + " allowed.";
				throw new IllegalArgumentException(m);
			}
		}
	}

	private static void verifyTuple(String iName, String s1, Iterator<Attribute> i, String oName, String s2,
			Iterator<Attribute> o) {

		while (i.hasNext()) {
			Attribute ai = i.next();
			Attribute ao = o.next();
			if (ai.getType().getMetaType() == Type.MetaType.TUPLE) {
				if (ao.getType().getMetaType() != Type.MetaType.TUPLE) {
					String m = getDesc(iName, ai) + " " + getDesc(oName, ao)
							+ " : both streams should have tuple type at the same place";
					throw new IllegalArgumentException(m);
				}
				TupleType it = (TupleType) ai.getType();
				TupleType ot = (TupleType) ao.getType();
				verify(iName, s1, it.getTupleSchema(), oName, s2, ot.getTupleSchema());
			}
		}
	}

	private static void verify(String in1, String in2, StreamSchema iS, String ou1, String ou2, StreamSchema oS) {
		if (iS.getAttributeCount() != oS.getAttributeCount())
			throw new IllegalArgumentException("Number of attributes of input stream " + in1 + " "
					+ iS.getAttributeCount() + " and for corresponding output stream " + ou1 + " "
					+ oS.getAttributeCount() + " does not match");
		verifyType(ou1, ou2, oS.iterator(), new Type.MetaType[] { Type.MetaType.BLOB, Type.MetaType.TUPLE });
		verifyType(in1, in2, iS.iterator(), aType);
		verifyTuple(in1, in2, iS.iterator(), ou1, ou2, oS.iterator());
	}

	@ContextCheck(runtime = true, compile = false)
	public static void verifyInputAndOutputStream(OperatorContextChecker checker) {

		OperatorContext c = checker.getOperatorContext();

		echo("checker ............");
		Logger.getLogger(JConvert.class).trace("Operator " + c.getName() + "checkContext: " + c.getPE().getPEId()
				+ " in Job: " + c.getPE().getJobId());
		if (c.getNumberOfStreamingInputs() != c.getNumberOfStreamingOutputs())
			throw new IllegalArgumentException("Number of input streams:" + c.getNumberOfStreamingInputs()
					+ " and number of output streams " + c.getNumberOfStreamingOutputs() + " does not match");
		for (int i = 0; i < c.getNumberOfStreamingInputs(); i++) {
			StreamingInput<Tuple> iList = c.getStreamingInputs().get(i);
			StreamingOutput<OutputTuple> oList = c.getStreamingOutputs().get(i);
			StreamSchema iS = iList.getStreamSchema();
			StreamSchema oS = oList.getStreamSchema();
			verify("Input stream " + iList.getName(), "input", iS, "Output stream " + oList.getName(), "output", oS);
		}

	}

	/**
	 * Initialize this operator. Called once before any tuples are processed.
	 * 
	 * @param context
	 *            OperatorContext for this operator.
	 * @throws Exception
	 *             Operator failure, will cause the enclosing PE to terminate.
	 */
	@Override
	public synchronized void initialize(OperatorContext context) throws Exception {
		// Must call super.initialize(context) to correctly setup an operator.
		super.initialize(context);
		Logger.getLogger(this.getClass()).trace("Operator " + context.getName() + " initializing in PE: "
				+ context.getPE().getPEId() + " in Job: " + context.getPE().getJobId());

		// TODO:
		// If needed, insert code to establish connections or resources to
		// communicate an external system or data store.
		// The configuration information for this may come from parameters
		// supplied to the operator invocation,
		// or external configuration files or a combination of the two.
	}

	/**
	 * Notification that initialization is complete and all input and output
	 * ports are connected and ready to receive and submit tuples.
	 * 
	 * @throws Exception
	 *             Operator failure, will cause the enclosing PE to terminate.
	 */
	@Override
	public synchronized void allPortsReady() throws Exception {
		// This method is commonly used by source operators.
		// Operators that process incoming tuples generally do not need this
		// notification.
		OperatorContext context = getOperatorContext();
		Logger.getLogger(this.getClass()).trace("Operator " + context.getName() + " all ports are ready in PE: "
				+ context.getPE().getPEId() + " in Job: " + context.getPE().getJobId());
	}

	private byte[] transformToB(int c, Tuple souT, Type.MetaType t) {
		HVal h;
		BIGSQLTYPE ttype = null;
		switch (t) {
		case BOOLEAN:
			h = HVal.createBool(souT.getBoolean(c));
			break;
		case INT64:
		case UINT64:
			h = HVal.createLong(souT.getLong(c));
			break;
		case INT8:
		case UINT8:
			ttype = BIGSQLTYPE.TINYINT;
			h = HVal.createInt(souT.getInt(c));
			break;
		case INT32:
		case INT16:
		case UINT32:
		case UINT16:
			h = HVal.createInt(souT.getInt(c));
			break;
		case RSTRING:
			h = HVal.createS(souT.getString(c));
			break;
		case FLOAT32:
			h = HVal.createFloat(souT.getFloat(c));
			break;
		case FLOAT64:
			h = HVal.createDouble(souT.getDouble(c));
			break;
		case TIMESTAMP:
			Timestamp ta = souT.getTimestamp(c);
			h = HVal.createTimestamp(ta.getSQLTimestamp());
			break;
		case DECIMAL32:
		case DECIMAL64:
		case DECIMAL128:
			h = HVal.createDecimal(souT.getBigDecimal(c));
			break;
		default:
			h = null;
			break; // unlikely
		}
		return h.toB(ttype);
	}

	private Tuple toVals(Tuple tuple, StreamSchema oS) {
		StreamSchema iS = tuple.getStreamSchema();
		int count = iS.getAttributeCount();
		List<Object> valO = new ArrayList<Object>();
		for (int a = 0; a < count; a++) {
			Type.MetaType t = iS.getAttribute(a).getType().getMetaType();
			if (t == Type.MetaType.TUPLE) {
				TupleType ot = (TupleType) oS.getAttribute(a).getType();
				Tuple tut = tuple.getTuple(a);
				// recursive
				Tuple outT = toVals(tut, ot.getTupleSchema());
				valO.add(outT);
			} else {
				byte[] b = transformToB(a, tuple, t);
				Blob bb = ValueFactory.newBlob(b);
				valO.add(bb);
			}
		}
		return oS.getTuple(valO);
	}

	/**
	 * Process an incoming tuple that arrived on the specified port.
	 * <P>
	 * Copy the incoming tuple to a new output tuple and submit to the output
	 * port.
	 * </P>
	 * 
	 * @param inputStream
	 *            Port the tuple is arriving on.
	 * @param tuple
	 *            Object representing the incoming tuple.
	 * @throws Exception
	 *             Operator failure, will cause the enclosing PE to terminate.
	 */
	@Override
	public final void process(StreamingInput<Tuple> iList, Tuple tuple) throws Exception {

		OperatorContext c = getOperatorContext();
		StreamingOutput<OutputTuple> oList = c.getStreamingOutputs().get(iList.getPortNumber());
		StreamSchema oS = oList.getStreamSchema();
		oList.submit(toVals(tuple, oS));
	}

	/**
	 * Process an incoming punctuation that arrived on the specified port.
	 * 
	 * @param stream
	 *            Port the punctuation is arriving on.
	 * @param mark
	 *            The punctuation mark
	 * @throws Exception
	 *             Operator failure, will cause the enclosing PE to terminate.
	 */
	@Override
	public void processPunctuation(StreamingInput<Tuple> stream, Punctuation mark) throws Exception {
		// For window markers, punctuate all output ports
		super.processPunctuation(stream, mark);
	}

	/**
	 * Shutdown this operator.
	 * 
	 * @throws Exception
	 *             Operator failure, will cause the enclosing PE to terminate.
	 */
	public synchronized void shutdown() throws Exception {
		OperatorContext context = getOperatorContext();
		Logger.getLogger(this.getClass()).trace("Operator " + context.getName() + " shutting down in PE: "
				+ context.getPE().getPEId() + " in Job: " + context.getPE().getJobId());

		// TODO: If needed, close connections or release resources related to
		// any external system or data store.

		// Must call super.shutdown()
		super.shutdown();
	}
}
