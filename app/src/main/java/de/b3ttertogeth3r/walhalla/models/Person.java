package de.b3ttertogeth3r.walhalla.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.Exclude;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.b3ttertogeth3r.walhalla.enums.Address;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.interfaces.ChangeListener;

public class Person implements Cloneable {
    //region static Variables
    public static final String ADDRESS = "address";
    public static final String ADDRESS_CITY = Address.CITY.toString();
    public static final String ADDRESS_NUMBER = Address.NUMBER.toString();
    public static final String ADDRESS_STREET = Address.STREET.toString();
    public static final String ADDRESS_ZIP_CODE = Address.ZIP.toString();
    public static final String ADDRESS_2 = "address_2";
    public static final String BALANCE = "balance";
    public static final String DOB = "doB";
    public static final String FIRST_NAME = "first_Name";
    public static final String FCM_TOKEN = "fcm_token";
    public static final String ID = "ID";
    public static final String JOINED = "joined";
    public static final String LAST_NAME = "last_Name";
    public static final String MAIL = "mail";
    public static final String MAJOR = "major";
    public static final String MOBILE = "mobile";
    public static final String PICTURE_PATH = "picture_path";
    public static final String POB = "poB";
    public static final String RANK = "rank";
    private static final String TAG = "Person";
    //endregion
    //region values
    /** not editable */
    private final boolean isVerified;
    /** not editable */
    private final boolean isDisabled;
    private boolean isPassword;
    private String id = "";
    private String PoB = "";
    private String first_Name = "";
    private String last_Name = "";
    private String email = "";
    private String mobile = "";
    private String rank = "";
    private String major = "";
    private Map<String, Object> address = new HashMap<>();
    private Map<String, Object> address_2 = new HashMap<>();
    private int joined = 0;
    private Timestamp DoB = new Timestamp(new Date());
    private float balance = 0f;
    private String picture_path = "";
    private String fcm_token = "";
    private ChangeListener<Person> changeListener = null;
    private String password;
    //endregion

    //region constructors

    /**
     * creating a new person with no values.
     *
     * @see Person Class description
     */
    public Person () {
        isVerified = false;
        isDisabled = false;
        isPassword = false;
    }

    public Person (@NonNull Charge charge) {
        this.address = charge.getAddress();
        this.first_Name = charge.getFirst_Name();
        this.last_Name = charge.getLast_Name();
        this.id = charge.getId();
        this.picture_path = charge.getPicture_path();
        this.PoB = charge.getPoB();
        this.major = charge.getMajor();
        this.mobile = charge.getMobile();
        this.email = charge.getMail();
        isVerified = false;
        isDisabled = false;
        isPassword = false;
    }

    /**
     * Creating a new Person with all values set by the user/this constructor
     *
     * @param doB
     *         Date of birth
     * @param poB
     *         Place of birth
     * @param first_Name
     *         First names
     * @param last_Name
     *         Sir name
     * @param mail
     *         e-mail address
     * @param mobile
     *         mobile or landline number
     * @param rank
     *         name of the rank
     * @param address
     *         address with {@link #ADDRESS_NUMBER} {@link #ADDRESS_STREET} {@link
     *         #ADDRESS_ZIP_CODE} {@link #ADDRESS_CITY}
     * @param address_2
     *         second address with {@link #ADDRESS_NUMBER} {@link #ADDRESS_STREET} {@link
     *         #ADDRESS_ZIP_CODE} {@link #ADDRESS_CITY}
     * @param joined
     *         the id of the joined semester
     * @param balance
     *         the value of the person
     * @param picture_path
     *         string to the cloud storage bucket with the image inside
     * @param major
     *         the major or the occupation title
     * @param id
     *         id of the user
     * @param fcm_token
     *         the tokens used to send push messages to this user.
     */
    public Person (String id, String poB, String first_Name, String last_Name, String mail,
                   String mobile, String rank, String major, Map<String, Object> address,
                   Map<String, Object> address_2, int joined, Timestamp doB, float balance,
                   String picture_path, String fcm_token, boolean isVerified, boolean isDisabled,
                   boolean hasPassword) {
        this.fcm_token = fcm_token;
        this.id = id;
        this.PoB = poB;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.email = mail;
        this.mobile = mobile;
        this.rank = rank;
        this.major = major;
        this.address = address;
        this.address_2 = address_2;
        this.joined = joined;
        this.DoB = doB;
        this.balance = balance;
        this.picture_path = picture_path;
        this.isVerified = isVerified;
        this.isDisabled = isDisabled;
        this.isPassword = hasPassword;
    }
    //endregion

    //region Getter and Setter

    /**
     *
     * @param password String password for the new auth user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * only for firebase to create a new user
     * @return normally null
     */
    public String getPassword(){
        return password;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    @Exclude
    @Deprecated
    public Map<String, Object> getAddress_2 () {
        return address_2;
    }

    @Exclude
    @Deprecated
    public void setAddress_2 (Map<String, Object> address_2) {
        this.address_2 = address_2;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    /**
     * @return value of {@code first_Name} and {@code last_Name} combined with a space
     * @see #getFirst_Name()
     * @see #getLast_Name()
     * @since 1.1
     */
    @NotNull
    @Exclude
    public String getFullName () {
        return getFirst_Name() + " " + getLast_Name();
    }

    public String getFirst_Name () {
        return first_Name;
    }

    public void setFirst_Name (String first_Name) {
        this.first_Name = first_Name;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    public String getLast_Name () {
        return last_Name;
    }

    public void setLast_Name (String last_Name) {
        this.last_Name = last_Name;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    /**
     * @return a cloned instance of this class.
     * @throws CloneNotSupportedException
     *         Thrown to indicate that the <code>clone</code> method in class
     *         <code>Object</code> has been called to clone an object, but that
     *         the object's class does not implement the <code>Cloneable</code>
     *         interface.
     * @throws UnsupportedOperationException
     *         Thrown if any of the sub functions throw this error
     * @see CloneNotSupportedException
     * @see UnsupportedOperationException
     * @see Cloneable
     * @see Object#clone()
     * @since 1.7
     */
    @NonNull
    @Exclude
    @NotNull
    public Object clone () throws CloneNotSupportedException, UnsupportedOperationException {
        return super.clone();
    }

    /**
     * needed because update of custom classes to firebase is impossible
     *
     * @return mapped values of the {@link #Person()}
     */
    @Exclude
    public Map<String, Object> toMap () {
        Map<String, Object> data = new HashMap<>();

        data.put(ADDRESS, getAddress());
        data.put(BALANCE, getBalance());
        data.put(DOB, getDoB());
        data.put(FIRST_NAME, getFirst_Name());
        data.put(JOINED, getJoined());
        data.put(LAST_NAME, getLast_Name());
        data.put(MAIL, getEmail());
        data.put(MAJOR, getMajor());
        data.put(MOBILE, getMobile());
        data.put(PICTURE_PATH, getPicture_path());
        data.put(POB, getPoB());
        data.put(RANK, getRank());
        data.put(FCM_TOKEN, getFcm_token());

        return data;
    }

    public Map<String, Object> getAddress () {
        return address;
    }

    public void setAddress (Map<String, Object> address) {
        this.address = address;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    public float getBalance () {
        return balance;
    }

    public Timestamp getDoB () {
        return DoB;
    }

    public int getJoined () {
        return joined;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getMajor () {
        return major;
    }

    public String getMobile () {
        return mobile;
    }

    public void setMobile (String mobile) {
        this.mobile = mobile;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    public String getPicture_path () {
        return picture_path;
    }

    public String getPoB () {
        return PoB;
    }

    public void setPoB (String poB) {
        PoB = poB;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    public String getRank () {
        return rank;
    }

    public void setRank (String rank) {
        this.rank = rank;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    public String getFcm_token () {
        return fcm_token;
    }

    public void setFcm_token (String fcm_token) {
        this.fcm_token = fcm_token;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    public void setPicture_path (String picture_path) {
        this.picture_path = picture_path;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    public void setMajor (String major) {
        this.major = major;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    public void setJoined (int joined) {
        this.joined = joined;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    public void setDoB (Timestamp doB) {
        DoB = doB;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    public void setBalance (float balance) {
        this.balance = balance;
        if(changeListener != null){
            changeListener.change(this);
        }
    }

    /**
     * not editable
     *
     * @return true, if Firebase Auth email is verified
     */
    public boolean isVerified () {
        return isVerified;
    }

    /**
     * not editable
     *
     * @return true, if Firebase Auth account is disabled
     */
    public boolean isDisabled () {
        return isDisabled;
    }

    //endregion

    /**
     * formatted into a string in german format. Example:
     * <blockquote>
     * 11.09.1864
     * </blockquote>
     * for November 11th, 1864
     *
     * @return String value of the date
     * @see #getDoB()
     */
    @NotNull
    @Exclude
    public String getDoBString () {
        try {
            Date date = getDoB().toDate();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.YEAR);
        } catch (Exception e) {
            Crashlytics.error(TAG, "Date invalid", e);
            return null;
        }
    }

    @Exclude
    public Set<String> getSet () {
        Set<String> set = new HashSet<>();
        set.add(this.fcm_token);
        set.add(this.id);
        set.add(this.PoB);
        set.add(this.first_Name);
        set.add(this.last_Name);
        set.add(this.email);
        set.add(this.mobile);
        set.add(this.rank);
        set.add(this.major);
        set.add(getAddressString());
        set.add(String.valueOf(this.joined));
        set.add(String.valueOf(this.DoB.getSeconds()));
        set.add(String.valueOf(this.balance));
        set.add(this.picture_path);

        return set;
    }

    /**
     * formatted into a string with a line break. Example:
     * <blockquote><pre>
     *     Mergentheimer Straße 32
     *     97082 Würzburg
     * </pre></blockquote>
     *
     * @return value of the address as a String
     * @throws NullPointerException
     *         if {@link #address} is empty
     * @see #getAddress()
     */
    @NotNull
    @Exclude
    public String getAddressString () {
        String result = "";
        try {
            result = address.get(Person.ADDRESS_STREET).toString() + " " +
                    address.get(Person.ADDRESS_NUMBER).toString() + "\n" +
                    address.get(Person.ADDRESS_ZIP_CODE).toString() + " " +
                    address.get(Person.ADDRESS_CITY).toString();
        } catch (Exception ignored) {
        }
        return result;
    }

    public boolean isValid () {
        try {
            return // !PoB.isEmpty() &&
                    !first_Name.isEmpty() &&
                    !last_Name.isEmpty() &&
                    // !mobile.isEmpty() &&
                    // !address.isEmpty() &&
                    !rank.isEmpty() &&
                    !email.isEmpty();
                    // DoB != null;
        } catch (Exception e) {
            Log.e(TAG, "isValid: ", e);
            return false;
        }
    }

    @Exclude
    public void setChangeListener(ChangeListener<Person> changeListener) {
        this.changeListener = changeListener;
    }

    public boolean isPassword () {
        return isPassword;
    }

    public void setPassword (boolean password) {
        isPassword = password;
    }
}
