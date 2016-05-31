import java.util.ArrayList;

/**
 * ģ��ALU���������͸���������������
 * 
 * @author [151250206_���ߺ�]
 *
 */

public class ALU {

	/**
	 * ����ʮ���������Ķ����Ʋ����ʾ��<br/>
	 * ����integerRepresentation("9", 8)
	 * 
	 * @param number
	 *            ʮ������������Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length
	 *            �����Ʋ����ʾ�ĳ���
	 * @return number�Ķ����Ʋ����ʾ������Ϊlength
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
	 * ����ʮ���Ƹ������Ķ����Ʊ�ʾ�� ��Ҫ���� 0������񻯡����������+Inf���͡�-Inf������ NaN�����أ������� IEEE 754��
	 * �������Ϊ��0���롣<br/>
	 * ����floatRepresentation("11.375", 8, 11)
	 * 
	 * @param number
	 *            ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return number�Ķ����Ʊ�ʾ������Ϊ 1+eLength+sLength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String floatRepresentation(String number, int eLength, int sLength) {
		// TODO YOUR CODE HERE.

		return null;
	}

	/**
	 * ����ʮ���Ƹ�������IEEE 754��ʾ��Ҫ�����{@link #floatRepresentation(String, int, int)
	 * floatRepresentation}ʵ�֡�<br/>
	 * ����ieee754("11.375", 32)
	 * 
	 * @param number
	 *            ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length
	 *            �����Ʊ�ʾ�ĳ��ȣ�Ϊ32��64
	 * @return number��IEEE 754��ʾ������Ϊlength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String ieee754(String number, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * ��������Ʋ����ʾ����������ֵ��<br/>
	 * ����integerTrueValue("00001001")
	 * 
	 * @param operand
	 *            �����Ʋ����ʾ�Ĳ�����
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 */
	public String integerTrueValue(String operand) {
		// TODO YOUR CODE HERE.
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
	 * ���������ԭ���ʾ�ĸ���������ֵ��<br/>
	 * ����floatTrueValue("01000001001101100000", 8, 11)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ����������ֱ��ʾΪ��+Inf���͡�-Inf����
	 *         NaN��ʾΪ��NaN��
	 */
	public String floatTrueValue(String operand, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * ��λȡ��������<br/>
	 * ����negation("00001001")
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @return operand��λȡ���Ľ��
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
	 * ���Ʋ�����<br/>
	 * ����leftShift("00001001", 2)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param n
	 *            ���Ƶ�λ��
	 * @return operand����nλ�Ľ��
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
	 * �߼����Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param n
	 *            ���Ƶ�λ��
	 * @return operand�߼�����nλ�Ľ��
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
	 * �������Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param n
	 *            ���Ƶ�λ��
	 * @return operand��������nλ�Ľ��
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
	 * ȫ����������λ�Լ���λ���мӷ����㡣<br/>
	 * ����fullAdder('1', '1', '0')
	 * 
	 * @param x
	 *            ��������ĳһλ��ȡ0��1
	 * @param y
	 *            ������ĳһλ��ȡ0��1
	 * @param c
	 *            ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ��ӵĽ�����ó���Ϊ2���ַ�����ʾ����1λ��ʾ��λ����2λ��ʾ��
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
	 * 4λ���н�λ�ӷ�����<br/>
	 * ����claAdder("1001", "0001", '1')
	 * 
	 * @param operand1
	 *            4λ�����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            4λ�����Ʊ�ʾ�ļ���
	 * @param c
	 *            ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ����Ϊ5���ַ�����ʾ�ļ����������е�1λ�����λ��λ����4λ����ӽ�������н�λ��������ѭ�����
	 */
	public String claAdder(String operand1, String operand2, char c) {
		// TODO YOUR CODE HERE.
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
	 * ��һ����ʵ�ֲ�������1�����㡣 ��Ҫģ��{@link #fullAdder(char, char, char) fullAdder}
	 * ��ʵ�֣��������Ե���{@link #fullAdder(char, char, char) fullAdder}��<br/>
	 * ����oneAdder("00001001")
	 * 
	 * @param operand
	 *            �����Ʋ����ʾ�Ĳ�����
	 * @return operand��1�Ľ��������Ϊoperand�ĳ��ȼ�1�����е�1λָʾ�Ƿ���������Ϊ1������Ϊ0��������λΪ��ӽ��
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
	 * �ӷ�����Ҫ�����{@link #claAdder(String, String, char) claAdder}����ʵ�֡�<br/>
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ļ���
	 * @param c
	 *            ���λ��λ
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String adder(String operand1, String operand2, char c, int length) {
		String result = "";
		// ��չ��������length����
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
			// ���������ţ����������
			result = "0" + result;
		} else {
			if (result.charAt(0) != operand1.charAt(0)) {
				// �������ͬ�ţ��ҽ���ķ���λ��ԭ���������Ų�ͬ�������
				result = "1" + result;
			} else {
				// �������ͬ�ţ��ҽ���ķ���λ��ԭ��������ͬ�������
				result = "0" + result;
			}
		}

		return result;
	}

	/**
	 * �����ӷ���Ҫ�����{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerAddition("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ļ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String integerAddition(String operand1, String operand2, int length) {
		String result = this.adder(operand1, operand2, '0', length);
		return result;
	}

	/**
	 * ����������Ҫ�����{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerSubtraction("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ļ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ��������
	 */
	public String integerSubtraction(String operand1, String operand2, int length) {
		String result = "";
		operand2 = this.oneAdder(this.negation(operand2)).substring(1);
		result = this.adder(operand1, operand2, '0', length);
		return result;
	}

	/**
	 * �����˷���ʹ��Booth�㷨ʵ�֣��ɵ���{@link #adder(String, String, char, int) adder}�ȷ�����
	 * <br/>
	 * ����integerMultiplication("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ĳ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ����˽�������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����˽��
	 */
	public String integerMultiplication(String operand1, String operand2, int length) {
		int n = 0;
		boolean overflow = false;
		boolean differentSigns = false;

		// �ж��Ƿ������ͬ
		if (operand1.charAt(0) != operand2.charAt(0)) {
			differentSigns = true;
		} else {
			differentSigns = false;
		}

		// ��չoperand2��length+1����
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
	 * �����Ĳ��ָ������������ɵ���{@link #adder(String, String, char, int) adder}�ȷ���ʵ�֡�<br/>
	 * ����integerDivision("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ĳ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ�ڸ�λ������λ
	 * @return ����Ϊ2*length+1���ַ�����ʾ�������������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0�������lengthλΪ�̣�
	 *         ���lengthλΪ����
	 */
	public String integerDivision(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �����������ӷ����ɵ���{@link #adder(String, String, char, int) adder} ����ʵ�֡�
	 * �����ŵ�ȷ��������Ƿ���������Ҫ��������㷨���У�����ֱ�ӽ�������תΪ�����ʾ��ʹ��integerAddition��
	 * integerSubtractionʵ�֡�<br/>
	 * ����signedAddition("1100", "1011", 8)
	 * 
	 * @param operand1
	 *            ������ԭ���ʾ�ı����������е�1λΪ����λ
	 * @param operand2
	 *            ������ԭ���ʾ�ļ��������е�1λΪ����λ
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ����������ţ�����ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ���䳤����չ��length
	 * @return ����Ϊlength+2���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������2λΪ����λ��
	 *         ��lengthλ����ӽ��
	 */
	public String signedAddition(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �������ӷ����ɵ���{@link #integerAddition(String, String, char, int)
	 * intergerAddition}�ȷ���ʵ�֡�<br/>
	 * ����floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ļ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength
	 *            ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����ӽ�������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0����
	 *         ����λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatAddition(String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �������������ɵ���{@link #floatAddition(String, String, int, int, int)
	 * floatAddition}����ʵ�֡�<br/>
	 * ����floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ļ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength
	 *            ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ�������������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0����
	 *         ����λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatSubtraction(String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �������˷����ɵ���{@link #integerAddition(String, String, char, int)
	 * integerAddition}�ȷ���ʵ�֡�<br/>
	 * ����floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ĳ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0����
	 *         ����λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatMultiplication(String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �������������ɵ���{@link #integerAddition(String, String, char, int)
	 * integerAddition}�ȷ���ʵ�֡�<br/>
	 * ����floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ĳ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0����
	 *         ����λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatDivision(String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * ����
	 * 
	 * @param x��һ��������
	 * @param y�ڶ���������
	 * @return ���
	 */
	public char and(char x, char y) {
		if (x == '0' || y == '0') {
			return '0';
		} else {
			return '1';
		}
	}

	/**
	 * ����
	 * 
	 * @param x��һ��������
	 * @param y�ڶ���������
	 * @return ���
	 */
	public char or(char x, char y) {
		if (x == '1' || y == '1') {
			return '1';
		} else {
			return '0';
		}
	}

	/**
	 * �����
	 * 
	 * @param x��һ��������
	 * @param y�ڶ���������
	 * @return ���
	 */
	public char xor(char x, char y) {
		if (x == y) {
			return '0';
		} else {
			return '1';
		}
	}

	/**
	 * ����
	 * 
	 * @param x������
	 * @return ���
	 */
	public char not(char x) {
		if (x == '1') {
			return '0';
		} else {
			return '1';
		}
	}
}
