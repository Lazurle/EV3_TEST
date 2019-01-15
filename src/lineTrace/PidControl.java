package lineTrace;

public class PidControl {

	public static final float DELTA_T = 0.001f;
    float[] diff = new float[2];
    float integral;

    //  sensor_val: センサー値, target_val: 目標値
    public float pid_sample(float sensor_val, float target_val,float KP,float KI,float KD) {
        float p, i, d;
        diff[0] = diff[1];
        diff[1] = sensor_val - target_val; // 偏差を取得
        integral += (diff[1] + diff[0]) / 2.0  * DELTA_T;

        p = KP * diff[1];
        i = KI * integral;
        d = KD * (diff[1] - diff[0]) / DELTA_T;


        // 最大・最小値を制限
        return math_limit(p + i + d, -360, 360);
    }

    public float math_limit(float val, float min, float max) {
        if (val < min) {
            return 0;
        } else if (val > max) {
            return 0;
        }
        return val;
    }
    //値の初期化
    public void reset(float sample){
    	diff[0] = 0;
    	diff[1] = 0;
    	integral = 0;
    }

}
