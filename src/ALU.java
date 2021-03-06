import java.util.ArrayList;

/**
 * 模拟ALU进行整数和浮点数的四则运算
 * 
 * @author [151250206_周沁涵]
 *
 */

public class ALU {

	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * 
	 * @param number
	 *            十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length
	 *            二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerRepresentation(String number, int length) {
		ArrayList<Integer> bits = new ArrayList<Integer>();

		int num = Integer.parseInt(number);

		if (num >= Math.pow(2.0, (double) (length - 1)) || num < -Math.pow(2.0, (double) (length - 1))) {
			return "Number Out Of Range!";
		}

		boolean isMinus = false;
		if (num < 0) {
			num = -num;
			isMinus = true;
		}

		for (int i = 0; (num / 2) != 0; i++) {

			bits.add(num % 2);
			num = num / 2;
		}

		if (number != "0") {
			bits.add(1);
		} else {
			bits.add(0);
		}

		String[] output = new String[length];
		for (int i = 0; i < length; i++) {
			output[i] = "0";
		}

		for (int i = 0; i < bits.size(); i++) {
			output[length - i - 1] = String.valueOf(bits.get(i));
		}

		String result = "";
		for (int i = 0; i < length; i++) {
			result += output[i];
		}

		if (isMinus) {
			result = this.oneAdder(this.negation(result)).substring(1);
		}
		return result;
	}

	/**
	 * 生成十进制浮点数的二进制表示。 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * 
	 * @param number
	 *            十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	// TODO NaN的情况
	public String floatRepresentation(String number, int eLength, int sLength) {
		String result = "";
		// 判断是否为负数
		boolean isMinus = false;
		if (number.startsWith("-")) {
			isMinus = true;
			number = number.substring(1);
		}

		double num = Double.parseDouble(number);
		// 考虑+0和-0的情况
		if (num == 0) {
			for (int i = 0; i < eLength + sLength; i++) {
				result += "0";
			}
			return (isMinus ? "1" : "0") + result;
		}
		// 考虑正负无穷的情况
		if (Math.abs(num) > (2 - Math.pow(2.0, -sLength)) * (int) Math.pow(2.0, (int) Math.pow(2.0, eLength - 1))) {
			for (int i = 0; i < eLength; i++) {
				result += "1";
			}
			for (int i = 0; i < sLength; i++) {
				result += "0";
			}
			return (isMinus ? "1" : "0") + result;
		}

		// 考虑NaN的情况

		int integerPart = (int) num;
		double fractionsPart = num - integerPart;
		// 分别得到整数部分和小数部分的二进制表示
		String integerRep = this.trueFormRepresentation(String.valueOf(integerPart));
		String fractionRep = this.fractionRepresentation(String.valueOf(fractionsPart), eLength + sLength);
		// 得到二进制表示的数，并保持小数点前只有一位
		int exponent = 0;// 指数
		String temp = integerRep + fractionRep;
		String numberInBinary = "";
		if (!number.startsWith("0")) {
			exponent = integerRep.length() - 1;
		} else {
			while (temp.startsWith("0")) {
				temp = this.leftShift(temp, 1);
				exponent--;
			}
		}
		numberInBinary = temp.substring(0, 1) + "." + temp.substring(1);
		// 考虑反规格化数的情况
		if (Math.abs(num) < Math.pow(2.0, Math.pow(2.0, -((int) Math.pow(2.0, eLength) - 2) / 2))) {
			for (int i = 0; i < eLength; i++) {
				result += "0";
			}
			numberInBinary = this.logRightShift(temp, 1);
			return (isMinus ? "1" : "0") + result + numberInBinary.substring(1, 1 + sLength);
		}

		// 表示指数
		String ex = this.trueFormRepresentation(String.valueOf(exponent + ((int) Math.pow(2.0, eLength) - 2) / 2));
		while (ex.length() < eLength) {
			ex = "0" + ex;
		}
		// 表示尾数
		String significand = numberInBinary.substring(2, 2 + sLength);

		result = (isMinus ? "1" : "0") + ex + significand;
		return result;
	}

	/**
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int)
	 * floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * 
	 * @param number
	 *            十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length
	 *            二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String ieee754(String number, int length) {
		int eLength32 = 8;
		int sLength32 = 23;
		int eLength64 = 11;
		int sLength64 = 52;
		String result = "";
		if (length == 32) {
			result = this.floatRepresentation(number, eLength32, sLength32);
		} else if (length == 64) {
			result = this.floatRepresentation(number, eLength64, sLength64);
		}
		return result;
	}

	/**
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * 
	 * @param operand
	 *            二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue(String operand) {
		char[] bits = operand.toCharArray();
		boolean isMinus = false;

		if (bits[0] == '1') {
			isMinus = true;
		}
		int sum = 0;
		for (int i = 1; i < operand.length(); i++) {
			sum += (bits[i] - '0') * (int) Math.pow(2.0, operand.length() - i - 1);
		}
		if (isMinus) {
			sum += Math.pow(-2.0, operand.length() - 1);
		}
		String result = String.valueOf(sum);

		return result;
	}

	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”，
	 *         NaN表示为“NaN”
	 */
	public String floatTrueValue(String operand, int eLength, int sLength) {
		boolean isMinus = false;
		boolean exponentAllOne = false;
		boolean exponentAllZero = false;
		boolean signiAllZero = false;

		if (operand.startsWith("1")) {
			isMinus = true;
		}

		String exponent = operand.substring(1, 1 + eLength);
		String significand = operand.substring(1 + eLength);

		char[] expo = exponent.toCharArray();
		char[] signi = significand.toCharArray();

		// +Inf,-Inf,NaN
		for (int i = 0; i < eLength; i++) {
			if (expo[i] == '1') {
				exponentAllOne = true;
			} else {
				exponentAllOne = false;
				break;
			}

		}
		for (int i = 0; i < sLength; i++) {
			if (signi[i] != '0') {
				signiAllZero = false;
				break;
			} else {
				signiAllZero = true;
			}
		}
		if (exponentAllOne && (!signiAllZero)) {
			return "NaN";
		}

		if (exponentAllOne && signiAllZero) {
			if (isMinus) {
				return "-Inf";
			} else {
				return "+Inf";
			}
		}

		// 0
		for (int i = 0; i < expo.length; i++) {
			if (expo[i] == '0') {
				exponentAllZero = true;
			} else {
				exponentAllZero = false;
				break;
			}
		}
		if (exponentAllZero && signiAllZero) {
			return "0";
		}

		// 反规格化非零数
		double abnormalNum = 0;
		if (exponentAllZero && !signiAllZero) {
			String f = "0" + significand;// 分数为0.f
			for (int i = 0; i < (Math.pow(2.0, eLength - 1) - 2); i++) {
				f = "0" + f;
			}
			for (int i = 0; i < f.substring(1).length(); i++) {
				abnormalNum += (f.substring(1).charAt(i) - '0') * (Math.pow(2.0, -(i + 1)));
			}

			return String.valueOf(abnormalNum);
		}

		// 规格化非零数
		int e = 0;
		for (int i = expo.length - 1; i >= 0; i--) {
			e += (int) Math.pow(2.0, eLength - i - 1) * (expo[i] - '0');
		}
		String temp = "1" + significand;
		String frontPoint = "";
		String behindPoint = "";
		// 若指数为正，则小数点需右移
		if (e - ((int) Math.pow(2.0, eLength - 1) - 1) > 0) {
			for (int i = 0; i < e - ((int) Math.pow(2.0, eLength - 1) - 1); i++) {
				temp = temp + "0";
			}
			frontPoint = temp.substring(0, e - ((int) Math.pow(2.0, eLength - 1) - 1) + 1);
			behindPoint = temp.substring(e - ((int) Math.pow(2.0, eLength - 1) - 1) + 1);
			// 若指数为负，则小数点左移
		} else if (e - ((int) Math.pow(2.0, eLength - 1) - 1) < 0) {
			for (int i = 0; i < -(e - ((int) Math.pow(2.0, eLength - 1) - 1)); i++) {
				temp = "0" + temp;
			}
			frontPoint = temp.substring(0, 1);
			behindPoint = temp.substring(1);
		} else if (e - ((int) Math.pow(2.0, eLength - 1) - 1) == 0) {
			frontPoint = temp.substring(0, 1);
			behindPoint = temp.substring(1);
		}

		// 根据temp，计算结果
		int integerPart = 0;// 整数部分
		double fractionPart = 0;// 小数部分
		for (int i = frontPoint.length() - 1; i >= 0; i--) {
			integerPart += (frontPoint.charAt(i) - '0') * ((int) Math.pow(2.0, frontPoint.length() - 1 - i));
		}
		for (int i = 0; i < behindPoint.length(); i++) {
			fractionPart += (behindPoint.charAt(i) - '0') * (Math.pow(2.0, -(i + 1)));
		}
		String result = String.valueOf(integerPart + fractionPart);

		if (isMinus) {
			result = "-" + result;
		}

		return result;
	}

	/**
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation(String operand) {
		char[] bits = new char[operand.length()];
		for (int i = 0; i < operand.length(); i++) {
			bits[i] = operand.charAt(i);
		}
		for (int i = 0; i < operand.length(); i++) {
			if (bits[i] == '0') {
				bits[i] = '1';
			} else if (bits[i] == '1') {
				bits[i] = '0';
			}
		}
		String result = "";
		for (int i = 0; i < operand.length(); i++) {
			result += bits[i];
		}
		return result;
	}

	/**
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            左移的位数
	 * @return operand左移n位的结果
	 */
	public String leftShift(String operand, int n) {
		char[] bits = new char[operand.length()];

		for (int i = 0; i < operand.length(); i++) {
			bits[i] = operand.charAt(i);
		}

		for (int j = 0; j < operand.length(); j++) {
			if (j + n < operand.length()) {
				bits[j] = bits[j + n];
			} else {
				bits[j] = '0';
			}
		}

		String result = "";
		for (int i = 0; i < operand.length(); i++) {
			result += bits[i];
		}

		return result;
	}

	/**
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            右移的位数
	 * @return operand逻辑右移n位的结果
	 */
	public String logRightShift(String operand, int n) {
		char[] bits = new char[operand.length()];

		for (int i = 0; i < operand.length(); i++) {
			bits[i] = operand.charAt(i);
		}

		for (int j = operand.length() - 1; j >= 0; j--) {
			if (j - n >= 0) {
				bits[j] = bits[j - n];
			} else {
				bits[j] = '0';
			}
		}

		String result = "";
		for (int i = 0; i < operand.length(); i++) {
			result += bits[i];
		}

		return result;
	}

	/**
	 * 算术右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            右移的位数
	 * @return operand算术右移n位的结果
	 */
	public String ariRightShift(String operand, int n) {
		char[] bits = new char[operand.length()];

		for (int i = 0; i < operand.length(); i++) {
			bits[i] = operand.charAt(i);
		}

		for (int j = operand.length() - 1; j >= 0; j--) {
			if (j - n >= 0) {
				bits[j] = bits[j - n];
			} else {
				if (bits[0] == '0') {
					bits[j] = '0';
				} else if (bits[0] == '1') {
					bits[j] = '1';
				}
			}
		}

		String result = "";
		for (int i = 0; i < operand.length(); i++) {
			result += bits[i];
		}

		return result;
	}

	/**
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * 
	 * @param x
	 *            被加数的某一位，取0或1
	 * @param y
	 *            加数的某一位，取0或1
	 * @param c
	 *            低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
	 */
	public String fullAdder(char x, char y, char c) {
		char sum = '0';
		char carrier = '0';

		sum = this.xor(x, this.xor(c, y));
		carrier = this.or(this.and(x, y), this.and(c, this.or(x, y)));

		String result = carrier + "" + sum;
		return result;
	}

	/**
	 * 4位先行进位加法器。<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * 
	 * @param operand1
	 *            4位二进制表示的被加数
	 * @param operand2
	 *            4位二进制表示的加数
	 * @param c
	 *            低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
	 */
	public String claAdder(String operand1, String operand2, char c) {
		char[] bits1 = operand1.toCharArray();
		char[] bits2 = operand2.toCharArray();
		char[] resultBits = new char[5];
		char[] ca = new char[5];

		char[] Pi = new char[4];
		char[] Gi = new char[4];
		for (int i = 0; i < 4; i++) {
			Pi[i] = or(bits1[i], bits2[i]);
			Gi[i] = and(bits1[i], bits2[i]);
		}
		ca[0] = c;
		ca[1] = or(Gi[3], and(Pi[3], ca[0]));
		ca[2] = or(Gi[2], or(and(Pi[2], Gi[3]), and(ca[0], and(Pi[3], Pi[2]))));
		ca[3] = or(Gi[1],
				or(and(Pi[1], Gi[2]), or(and(Pi[1], and(Pi[2], Gi[3])), and(and(Pi[1], Pi[2]), and(Pi[3], ca[0])))));
		ca[4] = or(Gi[0], or(and(Pi[0], Gi[1]), or(and(Pi[0], and(Pi[1], Gi[2])),
				or(and(and(Pi[0], Pi[1]), and(Pi[2], Gi[3])), and(and(Pi[0], Pi[1]), and(and(Pi[2], Pi[3]), ca[0]))))));

		for (int i = 0; i < 4; i++) {
			resultBits[5 - i - 1] = this.fullAdder(bits1[4 - i - 1], bits2[4 - i - 1], ca[i]).charAt(1);
		}
		resultBits[0] = ca[4];
		String result = "";
		for (int i = 0; i < 5; i++) {
			result += resultBits[i];
		}
		return result;
	}

	/**
	 * 加一器，实现操作数加1的运算。 需要模拟{@link #fullAdder(char, char, char) fullAdder}
	 * 来实现，但不可以调用{@link #fullAdder(char, char, char) fullAdder}。<br/>
	 * 例：oneAdder("00001001")
	 * 
	 * @param operand
	 *            二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder(String operand) {
		char[] bits = new char[operand.length() + 1];
		char[] carriers = new char[operand.length() + 1];

		bits[0] = '0';
		carriers[operand.length()] = '1';

		for (int i = 1; i <= operand.length(); i++) {
			bits[i] = operand.charAt(i - 1);
		}
		for (int i = operand.length(); i >= 1; i--) {
			carriers[i - 1] = this.and(bits[i], carriers[i]);
			bits[i] = this.xor(bits[i], carriers[i]);

		}

		String result = "";
		for (int i = 0; i < bits.length; i++) {
			result += bits[i];
		}
		return result;
	}

	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char) claAdder}方法实现。<br/>
	 * 
	 * @param operand1
	 *            二进制补码表示的被加数
	 * @param operand2
	 *            二进制补码表示的加数
	 * @param c
	 *            最低位进位
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，
	 *            需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder(String operand1, String operand2, char c, int length) {
		String result = "";
		// 扩展操作数至length长度
		while (operand1.length() < length) {
			operand1 = operand1.substring(0, 1) + operand1;
		}
		while (operand2.length() < length) {
			operand2 = operand2.substring(0, 1) + operand2;
		}

		for (int i = length - 1; i >= 3; i = i - 4) {
			result = this.claAdder(operand1.substring(i - 3, i + 1), operand2.substring(i - 3, i + 1), c).substring(1)
					+ result;
			c = this.claAdder(operand1.substring(i - 3, i + 1), operand2.substring(i - 3, i + 1), c).charAt(0);
		}

		if (operand1.charAt(0) != operand2.charAt(0)) {
			// 如果两数异号，不可能溢出
			result = "0" + result;
		} else {
			if (result.charAt(0) != operand1.charAt(0)) {
				// 如果两数同号，且结果的符号位与原操作数符号不同，则溢出
				result = "1" + result;
			} else {
				// 如果两数同号，且结果的符号位与原操作数相同，则不溢出
				result = "0" + result;
			}
		}

		return result;
	}

	/**
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被加数
	 * @param operand2
	 *            二进制补码表示的加数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，
	 *            需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition(String operand1, String operand2, int length) {
		String result = this.adder(operand1, operand2, '0', length);
		return result;
	}

	/**
	 * 整数减法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被减数
	 * @param operand2
	 *            二进制补码表示的减数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，
	 *            需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction(String operand1, String operand2, int length) {
		String result = "";
		operand2 = this.oneAdder(this.negation(operand2)).substring(1);
		result = this.adder(operand1, operand2, '0', length);
		return result;
	}

	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int) adder}等方法。
	 * <br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被乘数
	 * @param operand2
	 *            二进制补码表示的乘数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，
	 *            需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication(String operand1, String operand2, int length) {
		int n = 0;
		boolean overflow = false;
		boolean differentSigns = false;

		// 判断是否符号相同
		if (operand1.charAt(0) != operand2.charAt(0)) {
			differentSigns = true;
		} else {
			differentSigns = false;
		}

		// 扩展operand2至length+1长度
		while (operand2.length() < length) {
			operand2 = "0" + operand2;
		}
		operand2 = operand2 + "0";

		while (n < operand1.length()) {
			if ((operand2.charAt(operand2.length() - 1) - '0') - (operand2.charAt(operand2.length() - 2) - '0') == 0) {
				operand2 = this.ariRightShift(operand2, 1);
			} else if ((operand2.charAt(operand2.length() - 1) - '0')
					- (operand2.charAt(operand2.length() - 2) - '0') == 1) {
				operand2 = this.integerAddition(operand1, operand2.substring(0, operand1.length()), operand1.length())
						.substring(1) + operand2.substring(operand1.length());
				operand2 = this.ariRightShift(operand2, 1);
			} else if ((operand2.charAt(operand2.length() - 1) - '0')
					- (operand2.charAt(operand2.length() - 2) - '0') == -1) {
				operand2 = this
						.integerSubtraction(operand2.substring(0, operand1.length()), operand1, operand1.length())
						.substring(1) + operand2.substring(operand1.length());
				operand2 = this.ariRightShift(operand2, 1);
			}
			n++;
		}

		String result = operand2.substring(0, operand2.length() - 1);
		if ((differentSigns && result.charAt(0) == '0') || ((!differentSigns) && result.charAt(0) == '1')) {
			overflow = true;
		} else {
			overflow = false;
		}

		if (overflow) {
			result = "1" + result;
		} else {
			result = "0" + result;
		}

		return result;
	}

	/**
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被除数
	 * @param operand2
	 *            二进制补码表示的除数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，
	 *            需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，
	 *         最后length位为余数
	 */
	public String integerDivision(String operand1, String operand2, int length) {
		int n = 0;
		boolean overflow = false;
		boolean differentSigns = false;
		String quotient = operand1;
		String remainder = "";
		String result = "";
		// 判断是否符号相同
		if (operand1.charAt(0) != operand2.charAt(0)) {
			differentSigns = true;
		} else {
			differentSigns = false;
		}
		// 将被除数补至length长度，并将其放入quotient寄存器
		while (quotient.length() < length) {
			quotient = operand1.substring(0, 1) + quotient;
		}
		// 在被除数前扩展length位符号位，并存入余数寄存器
		for (int i = 0; i < length; i++) {
			remainder += operand1.substring(0, 1);
		}
		// 如果余数寄存器中的数与除数符号相同，做减，否则做加。
		// 若结果与除数符号相同，商寄存器后补1 否则补0。 左移;重复n次
		String temp = "";
		while (n < length) {
			if (remainder.charAt(0) == operand2.charAt(0)) {
				remainder = this.integerSubtraction(remainder, operand2, length).substring(1);
			} else {
				remainder = this.integerAddition(remainder, operand2, length).substring(1);
			}

			if (remainder.charAt(0) == operand2.charAt(0)) {
				quotient += "1";
			} else {
				quotient += "0";
			}

			temp = remainder + quotient;
			remainder = this.leftShift(temp, 1).substring(0, length);
			quotient = this.leftShift(temp, 1).substring(length, 2 * length);

			n++;
		}
		// 需要再做一次
		// 如果余数寄存器中的数与除数符号相同，做减，否则做加。
		// 若结果与除数符号相同，商寄存器后补1 否则补0。
		if (remainder.charAt(0) == operand2.charAt(0)) {
			remainder = this.integerSubtraction(remainder, operand2, length).substring(1);
		} else {
			remainder = this.integerAddition(remainder, operand2, length).substring(1);
		}

		if (remainder.charAt(0) == operand2.charAt(0)) {
			quotient += "1";
		} else {
			quotient += "0";
		}
		// 左移商，若商符号与除数相反，则+1
		quotient = this.leftShift(quotient, 1).substring(0, length);
		if (quotient.charAt(0) != operand2.charAt(0)) {
			quotient = this.oneAdder(quotient).substring(1);
		}
		// 若余数与除数符号相同，则余数减除数，否则做加法。
		if (remainder.charAt(0) == operand2.charAt(0)) {
			remainder = this.integerSubtraction(remainder, operand2, length).substring(1);
		} else {
			remainder = this.integerAddition(remainder, operand2, length).substring(1);
		}
		result = quotient + remainder;

		// 判断溢出条件
		if ((differentSigns && (quotient.charAt(0) == '1')) || ((!differentSigns) && (quotient.charAt(0) == '0'))) {
			overflow = false;
		} else {
			overflow = true;
		}
		// 添加溢出位
		if (overflow) {
			result = "1" + result;
		} else {
			result = "0" + result;
		}

		return result;
	}

	/**
	 * 带符号整数加法，可调用{@link #adder(String, String, char, int) adder} 方法实现。
	 * 但符号的确定、结果是否修正等需要按照相关算法进行，不能直接将操作数转为补码表示后使用integerAddition、
	 * integerSubtraction实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * 
	 * @param operand1
	 *            二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2
	 *            二进制原码表示的加数，其中第1位为符号位
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，
	 *            需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，
	 *         后length位是相加结果
	 */
	public String signedAddition(String operand1, String operand2, int length) {
		// 策略：如果两个数同号，则做加法，否则，做减法
		// *************加法：直接加*************
		// 忽略第一位符号位，后面的length-1位做加法，若最高位有进位，则意味着溢出；
		// 结果的符号和原来的加数、被加数的符号相同
		// *************减法：******************
		// 求operand2的补码
		// 对length-1位做加法，如果最高位有进位，则正确（符号与被减数相同）
		// 如果没有进位，正确结果为计算结果（length-1位）的补码（符号与被减数相反）

		boolean isOverflow = false;
		// 将操作数扩展到length+1长度,最前一位是符号位
		while (operand1.length() - 1 < length) {
			operand1 = operand1.substring(0, 1) + "0" + operand1.substring(1);
		}
		while (operand2.length() - 1 < length) {
			operand2 = operand2.substring(0, 1) + "0" + operand2.substring(1);
		}

		System.out.println(operand1 + "   " + operand2);
		String temp = "";
		String result = "";
		// 符号相同，则做加法
		if (operand1.charAt(0) == operand2.charAt(0)) {
			temp = this.adder(operand1.substring(1), operand2.substring(1), '0', length);
			if (temp.charAt(1) == '1') {
				isOverflow = true;
			}
			result = isOverflow ? "1" : "0" + operand1.substring(0, 1) + temp.substring(1);
		} else {
			operand2 = this.oneAdder(this.negation(operand2)).substring(1);
			temp = this.adder("0" + operand1.substring(1), "0" + operand2.substring(1), '0', length + 4);
			System.out.println(temp);
			if (temp.charAt(4) == '1') {
				result = "0" + operand1.substring(0, 1) + temp.substring(5);
			} else {
				result = "0" + this.negation(operand1.substring(0, 1))
						+ this.oneAdder(this.negation(temp.substring(5))).substring(1);
			}
		}

		return result;
	}

	/**
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int) SignedAddition}
	 * 等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被加数
	 * @param operand2
	 *            二进制表示的加数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @param gLength
	 *            保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），
	 *         其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	// TODO 测试特别的情况，似乎正常情况已经能够通过测试
	public String floatAddition(String operand1, String operand2, int eLength, int sLength, int gLength) {
		// 如果X为0或Y为0，则结果为另一个操作数
		// 直接返回
		if (this.floatTrueValue(operand1, eLength, sLength).equals("0")) {
			return "0" + operand2;
		}
		if (this.floatTrueValue(operand2, eLength, sLength).equals("0")) {
			return "0" + operand1;
		}
		// 保护位，长度为gLength，作用是右移时保留被移出的位
		String guardBits = "";
		for (int i = 0; i < gLength; i++) {
			guardBits += "0";
		}
		// 两个数都不为0
		String expo1 = operand1.substring(1, 1 + eLength);
		String signi1 = operand1.substring(1 + eLength);
		String expo2 = operand2.substring(1, 1 + eLength);
		String signi2 = operand2.substring(1 + eLength);

		int expo = 0;
		int expo1TrueValue = Integer.parseInt(this.integerTrueValue("0" + expo1));
		int expo2TrueValue = Integer.parseInt(this.integerTrueValue("0" + expo2));
		// 若指数不相等，则将小的增大，右移尾数，且将右移出的位放在保护位中
		// 如果右移至尾数等于0，则将另外一个数作为结果返回
		boolean signiAllZero = false;
		// temp为小数点前一位+尾数+保护位
		String temp1 = "1" + signi1 + guardBits;
		while (expo1TrueValue < expo2TrueValue) {
			expo = expo2TrueValue;
			temp1 = this.logRightShift(temp1, 1);
			expo1TrueValue++;
			for (int i = 1; i <= sLength; i++) {
				if (temp1.charAt(i) == '0') {
					signiAllZero = true;
				} else {
					signiAllZero = false;
					break;
				}
			}
			if (signiAllZero) {
				return "0" + operand2;
			}
		}

		String temp2 = "1" + signi2 + guardBits;
		while (expo2TrueValue < expo1TrueValue) {
			expo = expo1TrueValue;
			temp2 = this.logRightShift(temp2, 1);
			expo2TrueValue++;
			for (int i = 1; i <= sLength; i++) {
				if (temp2.charAt(i) == '0') {
					signiAllZero = true;
				} else {
					signiAllZero = false;
					break;
				}
			}
			if (signiAllZero) {
				return "0" + operand1;
			}
		}

		// 带符号尾数相加
		// 添加符号
		temp1 = operand1.substring(0, 1) + temp1;
		temp2 = operand2.substring(0, 1) + temp2;
		// while(temp1.length()<(int)((temp1.length()+2)/4)*4){
		// temp1=temp1.substring(0, 1)+"0"+temp1.substring(1);
		// temp2=temp2.substring(0, 1)+"0"+temp2.substring(1);
		// }
		// 相加结果为1位溢出位+1为符号位+无效位+结果
		String addResult = this.signedAddition(temp1, temp2,
				(int) ((temp1.length() + (temp1.length() % 4 == 0 ? 0 : 4)) / 4) * 4);
		// 结果为1位溢出位+1位符号位+结果
		String result = addResult.substring(0, 2)
				+ addResult.substring((int) (((temp1.length() + 2) / 4) * 4) - temp1.length() + 1);

		// 若结果的第一位不为1，则意味着尾数相加时没有产生进位至第一位，则第二位开始才是正确的结果
		if (result.charAt(2) != '1') {
			result = result.substring(1, 2) + result.substring(3);
		}

		boolean isOverflow = false;// 尾数是否溢出
		boolean resultOverflow = false;// 结果是否溢出（指数溢出）

		if (result.charAt(0) == '1') {
			isOverflow = true;
		}
		boolean resultIsMinus = false;
		if (result.charAt(1) == '1') {
			resultIsMinus = true;
		}
		// 若尾数溢出，则右移尾数，增量指数，若指数溢出，则将指数全设为1，尾数全为0，报告溢出
		if (isOverflow) {
			result = this.ariRightShift(result.substring(1), 1);
			expo++;
			if (expo >= ((int) Math.pow(2.0, eLength) - 1)) {
				resultOverflow = true;
			}
		}

		String returnResult = "";
		if (resultOverflow) {
			returnResult = resultIsMinus ? "1" : "0";
			for (int i = 0; i < eLength; i++) {
				returnResult = returnResult + "1";
			}
			for (int j = 0; j < sLength; j++) {
				returnResult = returnResult + "0";
			}

			return "1" + returnResult;
		}

		// 规格化结果
		while (result.charAt(2) != '1') {
			result = result.substring(0, 2) + this.leftShift(result.substring(2), 1);
			expo--;
			// 指数下溢
			if (expo <= 0) {
				returnResult = resultIsMinus ? "1" : "0";
				for (int i = 0; i < eLength; i++) {
					returnResult = returnResult + "0";
				}
				// 右移一位，使指数为-2^(2^eLength-2)
				returnResult = returnResult + this.logRightShift(result.substring(2), 1).substring(1, 1 + sLength);

				return resultOverflow ? "1" : "0" + returnResult;
			}
		}

		returnResult = resultIsMinus ? "1" : "0";
		if (this.trueFormRepresentation(String.valueOf(expo)).length() < eLength) {
			for (int i = 0; i < eLength - this.trueFormRepresentation(String.valueOf(expo)).length(); i++) {
				returnResult = returnResult + "0";
			}
		}
		returnResult = returnResult + this.trueFormRepresentation(String.valueOf(expo));

		returnResult = returnResult + result.substring(3, 3 + sLength);

		return resultOverflow ? "1" : "0" + returnResult;
	}

	/**
	 * 浮点数减法，要求调用{@link #floatAddition(String, String, int, int, int)
	 * floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被减数
	 * @param operand2
	 *            二进制表示的减数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @param gLength
	 *            保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），
	 *         其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	// TODO 测试
	public String floatSubtraction(String operand1, String operand2, int eLength, int sLength, int gLength) {
		if (operand2.startsWith("0")) {
			operand2 = "1" + operand2.substring(1);
		} else if (operand2.startsWith("1")) {
			operand2 = "0" + operand2.substring(1);
		}
		return this.floatAddition(operand1, operand2, eLength, sLength, gLength);
	}

	/**
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int)
	 * integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被乘数
	 * @param operand2
	 *            二进制表示的乘数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），
	 *         其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	// TODO 未测试
	public String floatMultiplication(String operand1, String operand2, int eLength, int sLength) {
		// 如果X为0或Y为0，则结果为0
		// 直接返回
		String returnResult = "";

		if (this.floatTrueValue(operand1, eLength, sLength).equals("0")
				|| this.floatTrueValue(operand2, eLength, sLength).equals("0")) {
			for (int i = 0; i < 2 + eLength + sLength; i++) {
				returnResult += "0";
			}

			return returnResult;
		}

		boolean resultIsMinus = false;
		if (operand1.charAt(0) != operand2.charAt(0)) {
			resultIsMinus = true;
		}

		// 指数相加，减偏值，若指数上溢，则返回+Inf/-Inf
		// 若指数下溢，则输出反规格化数
		// 若不溢出，则尾数相乘，再规格化
		boolean isOverflow = false;
		boolean isDownflow = false;

		String expo1 = operand1.substring(1, 1 + eLength);
		String signi1 = operand1.substring(1 + eLength);
		String expo2 = operand2.substring(1, 1 + eLength);
		String signi2 = operand2.substring(1 + eLength);

		int expo = 0;
		int expo1TrueValue = Integer.parseInt(this.integerTrueValue("0" + expo1));
		int expo2TrueValue = Integer.parseInt(this.integerTrueValue("0" + expo2));
		// 相加减偏值
		expo = expo1TrueValue + expo2TrueValue - (int) Math.pow(2.0, Math.pow(2.0, eLength - 1) - 1);

		if (expo >= (Math.pow(2.0, eLength) - 1)) {
			isOverflow = true;
		}
		if (expo <= 0) {
			isDownflow = true;
		}
		// 如果上溢，返回无穷
		if (isOverflow) {
			returnResult = resultIsMinus ? "1" : "0";
			for (int i = 0; i < eLength; i++) {
				returnResult = returnResult + "1";
			}
			for (int i = 0; i < sLength; i++) {
				returnResult = returnResult + "0";
			}

			return "1" + returnResult;
		}
		
		// 下溢
		String multiResult = "";
		String fraction = "";
		if (isDownflow) {
			multiResult = this.integerMultiplication("0" + signi1, "0" + signi2, ((int) (2 * sLength + 2) / 4) * 4);
			fraction = this.logRightShift(multiResult.substring(multiResult.length() - sLength, multiResult.length()),
					1);

			returnResult = (isOverflow ? "1" : "0") + (resultIsMinus ? "1" : "0");

			for (int i = 0; i < eLength; i++) {
				returnResult = returnResult + "0";
			}

			returnResult = returnResult + fraction;

			return returnResult;
		}

		multiResult = this.integerMultiplication("01" + signi1, "01" + signi2, ((int) (2 * sLength + 2) / 4) * 4);
		fraction = multiResult.substring(multiResult.indexOf("1"));
		// String behindPoint=fraction.substring(fraction.length()-2*sLength);
		fraction = this.leftShift(fraction, fraction.length() - 2 * sLength - 1);

		expo = expo + fraction.length() - 2 * sLength - 1;

		String e = this.trueFormRepresentation(String.valueOf(expo));
		if (e.length() < eLength) {
			e = "0" + e;
		}

		returnResult = resultIsMinus ? "1" : "0" + e + fraction.substring(0, sLength);
		return isOverflow ? "1" : "0" + returnResult;
	}

	/**
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int) integerDivision}
	 * 等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被除数
	 * @param operand2
	 *            二进制表示的除数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相除结果,其中第1位指示是否指数上溢（溢出为1，否则为0），
	 *         其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision(String operand1, String operand2, int eLength, int sLength) {
		//TODO 反规格化数（包括操作数为反规格化数，结果为反规格化数）
		// 如果X为0，则结果为0；如果Y为0，则结果为无穷，符号取决于X
		// 如果X和Y都不为0，则指数相减，并加偏值
		// 如果指数上溢，则返回无穷
		// 如果指数下溢，则返回反规格化数
		// 否则，尾数相除，规格化后返回
		String returnResult = "";
		// X为0，返回0
		if (this.floatTrueValue(operand1, eLength, sLength).equals("0")) {
			for (int i = 0; i < 2 + eLength + sLength; i++) {
				returnResult += "0";
			}

			return returnResult;
		}
		
		// Y为0，返回无穷
		if (this.floatTrueValue(operand2, eLength, sLength).equals("0")) {
			returnResult = "1" + operand1.substring(0, 1);
			for (int i = 0; i < eLength; i++) {
				returnResult = returnResult + "1";
			}
			for (int i = 0; i < sLength; i++) {
				returnResult = returnResult + "0";
			}

			return returnResult;
		}

		// 判断结果符号
		boolean resultIsMinus = false;
		if (operand1.charAt(0) != operand2.charAt(0)) {
			resultIsMinus = true;
		}

		boolean isOverflow = false;
		boolean isDownflow = false;

		String expo1 = operand1.substring(1, 1 + eLength);
		String signi1 = operand1.substring(1 + eLength);
		String expo2 = operand2.substring(1, 1 + eLength);
		String signi2 = operand2.substring(1 + eLength);

		int expo = 0;
		int expo1TrueValue = Integer.parseInt(this.integerTrueValue("0" + expo1));
		int expo2TrueValue = Integer.parseInt(this.integerTrueValue("0" + expo2));
		// 相减加偏值
		expo = expo1TrueValue - expo2TrueValue + (int) Math.pow(2.0, Math.pow(2.0, eLength - 1) - 1);

		if (expo >= (Math.pow(2.0, eLength) - 1)) {
			isOverflow = true;
		}
		if (expo <= 0) {
			isDownflow = true;
		}
		// 如果上溢，返回无穷
		if (isOverflow) {
			returnResult = resultIsMinus ? "1" : "0";
			for (int i = 0; i < eLength; i++) {
				returnResult = returnResult + "1";
			}
			for (int i = 0; i < sLength; i++) {
				returnResult = returnResult + "0";
			}

			return "1" + returnResult;
		}
		
		//未考虑操作数就是反规格化数的情况（乘法也是）
		//integerDivision传入的length一定是4的倍数？？
		String divResult=this.integerDivision("01"+signi1, "01"+signi2, sLength);
		String quotient=divResult.substring(1, 1+sLength);
		String remainder=divResult.substring(1+sLength);
		
		//正常情况
		String quotientComp="";
		for(int i=0;i<sLength;i++){
			quotientComp+="0";
		}
		//quotientComp:00...0(sLenth)
		
		
		if(quotient.equals(quotientComp)){
			while(!remainder.startsWith("1")){
			remainder=this.leftShift("0"+remainder, 1);
			expo--;
			}
		}
		String fraction=remainder.substring(1);

		returnResult="0"+(resultIsMinus?"1":"0");
		String e = this.trueFormRepresentation(String.valueOf(expo));
		if (e.length() < eLength) {
			e = "0" + e;
		}
		returnResult=returnResult+e+fraction;
		
		return returnResult;
	}

	/**
	 * 与门
	 * 
	 * @param x第一个操作数
	 * @param y第二个操作数
	 * @return 结果
	 */
	public char and(char x, char y) {
		if (x == '0' || y == '0') {
			return '0';
		} else {
			return '1';
		}
	}

	/**
	 * 或门
	 * 
	 * @param x第一个操作数
	 * @param y第二个操作数
	 * @return 结果
	 */
	public char or(char x, char y) {
		if (x == '1' || y == '1') {
			return '1';
		} else {
			return '0';
		}
	}

	/**
	 * 异或门
	 * 
	 * @param x第一个操作数
	 * @param y第二个操作数
	 * @return 结果
	 */
	public char xor(char x, char y) {
		if (x == y) {
			return '0';
		} else {
			return '1';
		}
	}

	/**
	 * 非门
	 * 
	 * @param x操作数
	 * @return 结果
	 */
	public char not(char x) {
		if (x == '1') {
			return '0';
		} else {
			return '1';
		}
	}

	/**
	 * 求一个正整数或0的原码表示
	 * 
	 * @param number
	 *            操作数，为正整数或0
	 * @return 二进制原码表示的数
	 */
	public String trueFormRepresentation(String number) {
		String result = "";
		int num = Integer.parseInt(number);
		while (num / 2 > 0) {
			result = String.valueOf(num % 2) + result;
			num /= 2;
		}
		if (num != 0) {
			result = "1" + result;
		} else {
			result = "0" + result;
		}
		return result;
	}

	/**
	 * 求一个小数的二进制表示
	 * 
	 * @param number
	 *            小数(0.f)
	 * @param length
	 *            所求二进制表示的长度
	 * @return 小数的二进制表示
	 */
	public String fractionRepresentation(String number, int length) {
		String result = "";
		double fraction = Double.parseDouble(number);

		while (result.length() < length) {
			result = result + String.valueOf((int) Math.floor(fraction * 2));

			fraction = fraction * 2 - Math.floor(fraction * 2);
		}
		return result;
	}

}
