package logbook.gui.widgets;

import java.util.Map.Entry;

import logbook.config.AppConfig;
import logbook.config.bean.AppConfigBean;
import logbook.internal.EvaluateExp;
import logbook.internal.SeaExp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * 設定画面-艦隊タブ
 *
 */
public final class FleettabConfigComposite extends Composite {

    /** 回数を表示 */
    private final Button displaycount;
    /** 海域 */
    private final Combo seacombo;
    /** 評価 */
    private final Combo evalcombo;
    /** 補給不足で警告アイコン表示 */
    private final Button warnByNeedSupply;
    /** 疲労状態で警告アイコン表示 */
    private final Button warnByCondState;
    /** 中破で警告アイコン表示 */
    private final Button warnByHalfDamage;
    /** 大破で致命的アイコン表示 */
    private final Button fatalBybadlyDamage;
    /** 大破でバルーンツールチップを表示 */
    private final Button balloonBybadlyDamage;
    /** 遠征からの帰還時に母港タブを表示 */
    private final Button visibleOnReturnMission;
    /** お風呂から上がる時に母港タブを表示 */
    private final Button visibleOnReturnBathwater;
    /** モノクロアイコンを使用 */
    private final Button useMonoIcon;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public FleettabConfigComposite(Composite parent, int style) {
        super(parent, style);

        this.setLayout(new GridLayout(1, false));

        Group leveling = new Group(this, SWT.NONE);
        leveling.setText("レベリング");
        leveling.setLayout(new RowLayout());
        leveling.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        this.displaycount = new Button(leveling, SWT.CHECK);
        this.displaycount.setText("回数を表示");
        this.displaycount.setSelection(AppConfig.get().isDisplayCount());

        Label label9 = new Label(leveling, SWT.NONE);
        label9.setText("海域");
        this.seacombo = new Combo(leveling, SWT.READ_ONLY);
        int count = 0;
        for (Entry<String, Integer> entry : SeaExp.get().entrySet()) {
            this.seacombo.add(entry.getKey());
            if (entry.getKey().equals(AppConfig.get().getDefaultSea())) {
                this.seacombo.select(count);
            }
            count++;
        }
        Label label10 = new Label(leveling, SWT.NONE);
        label10.setText("評価");
        this.evalcombo = new Combo(leveling, SWT.READ_ONLY);
        count = 0;
        for (Entry<String, Double> entry : EvaluateExp.get().entrySet()) {
            this.evalcombo.add(entry.getKey());
            if (entry.getKey().equals(AppConfig.get().getDefaultEvaluate())) {
                this.evalcombo.select(count);
            }
            count++;
        }

        this.warnByNeedSupply = new Button(this, SWT.CHECK);
        this.warnByNeedSupply.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        this.warnByNeedSupply.setText("補給不足で警告アイコン表示");
        this.warnByNeedSupply.setSelection(AppConfig.get().isWarnByNeedSupply());

        this.warnByCondState = new Button(this, SWT.CHECK);
        this.warnByCondState.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        this.warnByCondState.setText("疲労状態で警告アイコン表示");
        this.warnByCondState.setSelection(AppConfig.get().isWarnByCondState());

        this.warnByHalfDamage = new Button(this, SWT.CHECK);
        this.warnByHalfDamage.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        this.warnByHalfDamage.setText("中破で警告アイコン表示");
        this.warnByHalfDamage.setSelection(AppConfig.get().isWarnByHalfDamage());

        this.fatalBybadlyDamage = new Button(this, SWT.CHECK);
        this.fatalBybadlyDamage.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        this.fatalBybadlyDamage.setText("大破で致命的アイコン表示");
        this.fatalBybadlyDamage.setSelection(AppConfig.get().isFatalBybadlyDamage());

        this.balloonBybadlyDamage = new Button(this, SWT.CHECK);
        this.balloonBybadlyDamage.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        this.balloonBybadlyDamage.setText("大破でバルーンツールチップを表示");
        this.balloonBybadlyDamage.setSelection(AppConfig.get().isBalloonBybadlyDamage());

        this.visibleOnReturnMission = new Button(this, SWT.CHECK);
        this.visibleOnReturnMission.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        this.visibleOnReturnMission.setText("遠征からの帰還時に母港タブを表示");
        this.visibleOnReturnMission.setSelection(AppConfig.get().isVisibleOnReturnMission());

        this.visibleOnReturnBathwater = new Button(this, SWT.CHECK);
        this.visibleOnReturnBathwater.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        this.visibleOnReturnBathwater.setText("お風呂から上がる時に母港タブを表示");
        this.visibleOnReturnBathwater.setSelection(AppConfig.get().isVisibleOnReturnBathwater());

        this.useMonoIcon = new Button(this, SWT.CHECK);
        this.useMonoIcon.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        this.useMonoIcon.setText("モノクロアイコンを使用");
        this.useMonoIcon.setSelection(AppConfig.get().isMonoIcon());
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * 回数を表示を取得します。
     * @return 回数を表示
     */
    public boolean getDisplaycount() {
        return this.displaycount.getSelection();
    }

    /**
     * 海域を取得します。
     * @return 海域
     */
    public String getSea() {
        return this.seacombo.getItem(this.seacombo.getSelectionIndex());
    }

    /**
     * 評価を取得します。
     * @return 評価
     */
    public String getEval() {
        return this.evalcombo.getItem(this.evalcombo.getSelectionIndex());
    }

    /**
     * 補給不足で警告アイコン表示を取得します。
     * @return 補給不足で警告アイコン表示
     */
    public boolean getWarnByNeedSupply() {
        return this.warnByNeedSupply.getSelection();
    }

    /**
     * 疲労状態で警告アイコン表示を取得します。
     * @return 疲労状態で警告アイコン表示
     */
    public boolean getWarnByCondState() {
        return this.warnByCondState.getSelection();
    }

    /**
     * 中破で警告アイコン表示を取得します。
     * @return 中破で警告アイコン表示
     */
    public boolean getWarnByHalfDamage() {
        return this.warnByHalfDamage.getSelection();
    }

    /**
     * 大破で致命的アイコン表示を取得します。
     * @return 大破で致命的アイコン表示
     */
    public boolean getFatalBybadlyDamage() {
        return this.fatalBybadlyDamage.getSelection();
    }

    /**
     * 大破でバルーンツールチップを表示を取得します。
     * @return 大破でバルーンツールチップを表示
     */
    public boolean getBalloonBybadlyDamage() {
        return this.balloonBybadlyDamage.getSelection();
    }

    /**
     * 遠征からの帰還時に母港タブを表示を取得します。
     * @return 遠征からの帰還時に母港タブを表示
     */
    public boolean getVisibleOnReturnMission() {
        return this.visibleOnReturnMission.getSelection();
    }

    /**
     * お風呂から上がる時に母港タブを表示を取得します。
     * @return お風呂から上がる時に母港タブを表示
     */
    public boolean getVisibleOnReturnBathwater() {
        return this.visibleOnReturnBathwater.getSelection();
    }

    /**
     * モノクロアイコンを使用を取得します。
     * @return モノクロアイコンを使用
     */
    public boolean getUseMonoIcon() {
        return this.useMonoIcon.getSelection();
    }

    /**
     * 画面設定を登録します
     * @param config アプリケーション設定
     */
    public void setConfig(AppConfigBean config) {
        config.setDisplayCount(this.getDisplaycount());
        config.setDefaultSea(this.getSea());
        config.setDefaultEvaluate(this.getEval());
        config.setWarnByNeedSupply(this.getWarnByNeedSupply());
        config.setWarnByCondState(this.getWarnByCondState());
        config.setWarnByHalfDamage(this.getWarnByHalfDamage());
        config.setFatalBybadlyDamage(this.getFatalBybadlyDamage());
        config.setBalloonBybadlyDamage(this.getBalloonBybadlyDamage());
        config.setVisibleOnReturnMission(this.getVisibleOnReturnMission());
        config.setVisibleOnReturnBathwater(this.getVisibleOnReturnBathwater());
        config.setMonoIcon(this.getUseMonoIcon());
    }
}
