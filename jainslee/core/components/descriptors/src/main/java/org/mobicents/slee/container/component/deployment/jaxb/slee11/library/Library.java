//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.17 at 04:43:56 PM WET 
//


package org.mobicents.slee.container.component.deployment.jaxb.slee11.library;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "description",
    "libraryName",
    "libraryVendor",
    "libraryVersion",
    "libraryRef",
    "jar"
})
@XmlRootElement(name = "library")
public class Library {

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    protected Description description;
    @XmlElement(name = "library-name", required = true)
    protected LibraryName libraryName;
    @XmlElement(name = "library-vendor", required = true)
    protected LibraryVendor libraryVendor;
    @XmlElement(name = "library-version", required = true)
    protected LibraryVersion libraryVersion;
    @XmlElement(name = "library-ref")
    protected List<LibraryRef> libraryRef;
    protected List<Jar> jar;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the libraryName property.
     * 
     * @return
     *     possible object is
     *     {@link LibraryName }
     *     
     */
    public LibraryName getLibraryName() {
        return libraryName;
    }

    /**
     * Sets the value of the libraryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link LibraryName }
     *     
     */
    public void setLibraryName(LibraryName value) {
        this.libraryName = value;
    }

    /**
     * Gets the value of the libraryVendor property.
     * 
     * @return
     *     possible object is
     *     {@link LibraryVendor }
     *     
     */
    public LibraryVendor getLibraryVendor() {
        return libraryVendor;
    }

    /**
     * Sets the value of the libraryVendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link LibraryVendor }
     *     
     */
    public void setLibraryVendor(LibraryVendor value) {
        this.libraryVendor = value;
    }

    /**
     * Gets the value of the libraryVersion property.
     * 
     * @return
     *     possible object is
     *     {@link LibraryVersion }
     *     
     */
    public LibraryVersion getLibraryVersion() {
        return libraryVersion;
    }

    /**
     * Sets the value of the libraryVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link LibraryVersion }
     *     
     */
    public void setLibraryVersion(LibraryVersion value) {
        this.libraryVersion = value;
    }

    /**
     * Gets the value of the libraryRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the libraryRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLibraryRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LibraryRef }
     * 
     * 
     */
    public List<LibraryRef> getLibraryRef() {
        if (libraryRef == null) {
            libraryRef = new ArrayList<LibraryRef>();
        }
        return this.libraryRef;
    }

    /**
     * Gets the value of the jar property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jar property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJar().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Jar }
     * 
     * 
     */
    public List<Jar> getJar() {
        if (jar == null) {
            jar = new ArrayList<Jar>();
        }
        return this.jar;
    }

}