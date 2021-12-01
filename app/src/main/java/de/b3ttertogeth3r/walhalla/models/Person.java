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
import de.b3ttertogeth3r.walhalla.firebase.Firebase;

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
    public static final String RANK_SETTINGS = "rankSettings";
    public static final String UID = "uid";
    private static final String TAG = "Person";
    //endregion

    //region values
    private String id;
    private String PoB;
    private String first_Name;
    private String last_Name;
    private String mail;
    private String mobile;
    private String rank;
    private String uid;
    private String major;
    private Map<String, Object> address = new HashMap<>();
    private Map<String, Object> address_2 = new HashMap<>();
    private int joined = 0;
    private Timestamp DoB;
    private float balance = 0f;
    private String picture_path;
    private Map<String, Object> rankSettings = new HashMap<>();
    private String fcm_token;
    //endregion

    //region constructors

    /**
     * creating a new person with no values.
     *
     * @see Person Class description
     */
    public Person () {
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
     * @param uid
     *         uid of the firebase auth
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
     * @param rankSettings
     *         the customised settings by the user
     * @param fcm_token
     *         the tokens used to send push messages to this user.
     */
    public Person (String id, String poB, String first_Name, String last_Name, String mail,
                   String mobile, String rank, String uid, String major,
                   Map<String, Object> address, Map<String, Object> address_2, int joined,
                   Timestamp doB, float balance, String picture_path,
                   Map<String, Object> rankSettings, String fcm_token) {
        this.fcm_token = fcm_token;
        this.id = id;
        this.PoB = poB;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.mail = mail;
        this.mobile = mobile;
        this.rank = rank;
        this.uid = uid;
        this.major = major;
        this.address = address;
        this.address_2 = address_2;
        this.joined = joined;
        this.DoB = doB;
        this.balance = balance;
        this.picture_path = picture_path;
        this.rankSettings = rankSettings;
    }
    //endregion

    //region Getter and Setter
    @Exclude
    public String getId () {
        return id;
    }

    @Exclude
    public void setId (String id) {
        this.id = id;
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
    }

    public String getLast_Name () {
        return last_Name;
    }

    public void setLast_Name (String last_Name) {
        this.last_Name = last_Name;
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
        data.put(MAIL, getMail());
        data.put(MAJOR, getMajor());
        data.put(MOBILE, getMobile());
        data.put(PICTURE_PATH, getPicture_path());
        data.put(POB, getPoB());
        data.put(RANK, getRank());
        data.put(RANK_SETTINGS, getRankSettings());
        data.put(UID, getUid());
        data.put(FCM_TOKEN, getFcm_token());

        return data;
    }

    public Map<String, Object> getAddress () {
        return address;
    }

    public void setAddress (Map<String, Object> address) {
        this.address = address;
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

    public String getMail () {
        return mail;
    }

    public void setMail (String mail) {
        this.mail = mail;
    }

    public String getMajor () {
        return major;
    }

    public String getMobile () {
        return mobile;
    }

    public void setMobile (String mobile) {
        this.mobile = mobile;
    }

    public String getPicture_path () {
        return picture_path;
    }

    public String getPoB () {
        return PoB;
    }

    public void setPoB (String poB) {
        PoB = poB;
    }

    public String getRank () {
        return rank;
    }

    public void setRank (String rank) {
        this.rank = rank;
    }

    public Map<String, Object> getRankSettings () {
        return rankSettings;
    }

    public String getUid () {
        return uid;
    }

    public void setUid (String uid) {
        this.uid = uid;
    }

    public String getFcm_token () {
        return fcm_token;
    }

    public void setFcm_token (String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public void setRankSettings (Map<String, Object> rankSettings) {
        this.rankSettings = rankSettings;
    }

    public void setPicture_path (String picture_path) {
        this.picture_path = picture_path;
    }

    public void setMajor (String major) {
        this.major = major;
    }

    public void setJoined (int joined) {
        this.joined = joined;
    }


    public void setDoB (Timestamp doB) {
        DoB = doB;
    }

    public void setBalance (float balance) {
        this.balance = balance;
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
            Firebase.Crashlytics.log(TAG, "Date invalid", e);
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
        set.add(this.mail);
        set.add(this.mobile);
        set.add(this.rank);
        set.add(this.uid);
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
            return !this.PoB.isEmpty() &&
                    !first_Name.isEmpty() &&
                    !last_Name.isEmpty() &&
                    !mobile.isEmpty() &&
                    !major.isEmpty() &&
                    !address.isEmpty() &&
                    //TODO add rank into profile
                    this.DoB != null;
        } catch (Exception e) {
            Log.e(TAG, "isValid: ", e);
            return false;
        }
    }
}
