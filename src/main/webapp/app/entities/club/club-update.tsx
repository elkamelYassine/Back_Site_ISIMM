import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEtudiant } from 'app/shared/model/etudiant.model';
import { getEntities as getEtudiants } from 'app/entities/etudiant/etudiant.reducer';
import { IClub } from 'app/shared/model/club.model';
import { getEntity, updateEntity, createEntity, reset } from './club.reducer';

export const ClubUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const etudiants = useAppSelector(state => state.etudiant.entities);
  const clubEntity = useAppSelector(state => state.club.entity);
  const loading = useAppSelector(state => state.club.loading);
  const updating = useAppSelector(state => state.club.updating);
  const updateSuccess = useAppSelector(state => state.club.updateSuccess);

  const handleClose = () => {
    navigate('/club');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEtudiants({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...clubEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...clubEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="isimmManagerApp.club.home.createOrEditLabel" data-cy="ClubCreateUpdateHeading">
            <Translate contentKey="isimmManagerApp.club.home.createOrEditLabel">Create or edit a Club</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="club-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('isimmManagerApp.club.nom')} id="club-nom" name="nom" data-cy="nom" type="text" />
              <ValidatedField
                label={translate('isimmManagerApp.club.pageFB')}
                id="club-pageFB"
                name="pageFB"
                data-cy="pageFB"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.club.pageIg')}
                id="club-pageIg"
                name="pageIg"
                data-cy="pageIg"
                type="text"
              />
              <ValidatedField label={translate('isimmManagerApp.club.email')} id="club-email" name="email" data-cy="email" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/club" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ClubUpdate;
